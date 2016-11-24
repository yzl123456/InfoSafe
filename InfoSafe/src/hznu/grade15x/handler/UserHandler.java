package hznu.grade15x.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hznu.grade15x.MD5utils.MD5;
import hznu.grade15x.QRCode.QRCodeUtil;
import hznu.grade15x.entity.User;
import hznu.grade15x.service.UserService;
import hznu.grade15x.utils.GetRandomString;
import hznu.grade15x.utils.GoogleValidate;
/*
 * Controller类，处理一些响应的请求
 */
@Controller
public class UserHandler {
	//自动装配
	@Autowired
	private UserService userService;
	
	//处理对应requestMapping的请求，这个是用来做分页的，以前写的直接没改拿过来用了，用来将数据库的信息直接在页面上显示
	@RequestMapping("/users")
	public String list(@RequestParam(value="pageNo",defaultValue="1",required=false) String pageNoStr
			,Map<String,Object>map){
		int pageNo=Integer.parseInt(pageNoStr);
		Page<User> page = userService.getPage(pageNo,999);
		map.put("page", page);
		return "user/list";
	}
	
	//注册成功时的save操作，基于restful风格
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public String save(User user,@RequestParam(value="username",required=false) String username,
			@RequestParam(value="password",required=false) String password,Map<String, Object>map){
		if(username==""||username==null||password==""||password==null){
			map.put("myError", "请输入用户名或密码");
			return "user/register";
		}
		
		//新增s-key需求，给每个user创建serverMD5String，假定一轮s-key能够输入密码100000次
		String serverMD5String=MD5.getServerMD5String(password, 100000);
		user.setServerMD5String(serverMD5String);
		user.setUserLoginTime(0);
		
		userService.save(user);
		return "user/home";
	}
	
	//点击注册按钮的响应
	@RequestMapping("toRegisterPage")
	public String toRegisterPage(){
		return "user/register";
	}
	
	//第一次在谷歌APP上输入页面上的密钥后点击确定进入动态密码验证
	@RequestMapping("firstToTotp")
	public String toTotpPage(@RequestParam(value="id",required=false) Integer id,Map<String, Object>map){
		User user=userService.getByID(id);
		map.put("user", user);
		return "user/totp";
	}
	
	//点击登录按钮
	@RequestMapping("/login")
	public String login(HttpServletRequest request,@RequestParam(value="username",required=false) String username,
			@RequestParam(value="password",required=false) String password,Map<String, Object>map
			) throws Exception{
		//若密码或用户名为空，回到主页
		if(username==null||username==""||password==null||password==""){
			map.put("myError", "请输入用户名或密码");
			return "user/home";
		}
		
		User user=userService.getByName(username);
		if(user==null){
			map.put("myError", "用户名或密码错误");
			return "user/home";
		}
		
		
		//进行密码验证前根据登陆次数进行预处理
		password=MD5.prepareHash(password, user.getUserLoginTime());
		
		//密码正确
		if(MD5.stringMD5(password).equals(user.getServerMD5String())){
			//更新服务端的MD5String 和loginTime
			user.setUserLoginTime(user.getUserLoginTime()+1);
			user.setServerMD5String(password);
			userService.flush(user);
			
			//表中secretKey字段非空表示不是第一次登录
			if(user.getSecretKey()==null){
				//服务器端生成16位的随机数
				String randomNumber=GetRandomString.getRandomString(16);
				
				String secretKey=GoogleValidate.createCredentials(randomNumber);
				user.setSecretKey(secretKey);
				System.out.println("before---------------");
				
				//实体类中更新了secretKey字段后在数据库中刷新对应的数据
				userService.flush(user);
				
				System.out.println("after---------------");
				
				//生成对应id的二维码并且保存在QRCode目录下，文件名为id.jpg
				//特别要注意这里这里二维码不仅要存在项目workspace的目录下，而且在apache部署项目的目录下也要直接存，不然要刷新项目才能显示图片
				//通过request.getSession().getServletContext().getRealPath("/")得到部署项目的目录
				//先把生成的图片放到apache的部署目录，同时用IO流将图片放到当前项目目录下
					
				
				//我的apache部署项目的目录 D:\WEB_ADDRESS\wtpwebapps\InfoSafe\
			    String apachePath=request.getSession().getServletContext().getRealPath("/");
				String basePath=apachePath+"QRCode";
			
//				String url="otpauth://totp/Google%3Ayourname@gmail.com?secret="+secretKey+"&issuer=Google";
				String url="otpauth://totp/Google%3A"+username+"@gmail.com?secret="+secretKey+"&issuer=Google";

				
				//二维码的logo图片所在地址
				String logoPath=basePath+"//qq.png";
				//保存图片的id
				QRCodeUtil.id=user.getId()+"";
				//生成二维码 url->二维码的url链接  logopath-->logo图片所在的地址  basePath-->存放二维码的文件夹 true-->压缩二维码
				QRCodeUtil.encode(url, logoPath, basePath, true);
				
				
				/*
				 * IO流操作将apache部署目录下的图片复制到项目目录的QRCode下
				 */
				//本地工程目录，根据实际修改
				String projectPath="C://Users//泽林//Desktop//Github//InfoSafe//InfoSafe//WebContent";
				
				String localPath=projectPath+"//QRCode";
				File file=new File(localPath+"//qq.png");
				//如果本地路径改对了 能够找到文件
				if(file.exists()){
					/*
					 * IO流复制
					 */
					System.out.println("ready to copy..........");
					InputStream in=new FileInputStream(new File(basePath+"//"+user.getId()+".jpg"));
					OutputStream out=new FileOutputStream(new File(localPath+"//"+user.getId()+".jpg"));
					byte[] buffer=new byte[1024*10];
					int len=0;
					while((len=in.read(buffer))!=-1){
						out.write(buffer);
					}
					in.close();
					out.close();
				}
				
				
				
				//放到页面的requestScope中
				map.put("secretKey",secretKey);
				map.put("user", user);
				return "user/first_totp";
			}
			//第一次注册成功后登录
			else {
				map.put("user",user);
				if(user.getTotp()==1)
					return "user/totp";
				else
					return "emp/index";
			}
		}
		//密码错误
		else {
			System.out.println("TO home");
			map.put("myError", "用户名或密码错误");
			return "user/home";
		}
	}
	
	//动态密码验证
	@RequestMapping("/validateCode")
	public String validateCode(@RequestParam(value="code",required=false) String code,
			@RequestParam(value="id",required=false) String id,Map<String, Object>map) throws Exception{
		//定位到数据表中的当前用户
		User user=userService.getByID(Integer.parseInt(id));
		//获取当前用户在服务器端的动态密码
		String serverCode=GoogleValidate.getValidateCode(user.getSecretKey());
		System.out.println("Server code--------------"+serverCode);
		System.out.println("param code--------------"+code);
		//验证成功
		if(serverCode.equals(code)){
			return "redirect:loginSuccess";
		}
		//失败
		else{
			map.put("myError", "动态密码错误!");
			return "user/totp";
		}
	}
	
	//ajax请求验证用户名是否可用
	@ResponseBody
	@RequestMapping("/ajaxUserValidateName")
	public String validateName(@RequestParam(value="name",required=true) String name){
		User user=userService.getByName(name);
		//数据表中存在该用户，返回标记位 1 否则返回 0
		if(user==null)
			return "0";
		else return "1";
	}
	@RequestMapping("/loginSuccess")
	public String allSuccess(Map<String, Object>map){
		
		return "emp/index";
	}
	
	@RequestMapping("/toUserHome")
	public String toUserHome(){
		return "user/home";
	}
	
	
	@RequestMapping("/closeGoogle")
	public String closeGoogle(@RequestParam(value="id",required=true)Integer id){
		System.out.println(id+"------------");
		User user=userService.getByID(id);
		user.setTotp(0);
		userService.flush(user);
		return "emp/index_close";
	}
	
	@RequestMapping("/openGoogle")
	public String openGoogle(@RequestParam(value="id",required=true)Integer id){
		System.out.println(id+"------------");
		User user=userService.getByID(id);
		user.setTotp(1);
		userService.flush(user);
		return "emp/index_open";
	}
}
