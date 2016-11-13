package hznu.grade15x.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String login(@RequestParam(value="username",required=false) String username,
			@RequestParam(value="password",required=false) String password,Map<String, Object>map
			){
		//若密码或用户名为空，回到主页
		if(username==null||username==""||password==null||password==""){
			map.put("myError", "请输入用户名或密码");
			return "user/home";
		}
		
		User user=userService.getByName(username);
		
		System.out.println("-->"+password);
		System.out.println("-->"+user.getPassword());
		//密码正确
		if(password.equals(user.getPassword())){
			//表中secretKey字段非空表示不是第一次登录
			if(user.getSecretKey()==null){
				System.out.println("To Next");
				//获取16位纯数字的字符，特别要注意不能是字母的
				String seed=GetRandomString.getRandomString(16);
				
//				seed="1234abcd1234abcd";
				
				user.setSeed(seed);
				String secretKey=GoogleValidate.createCredentials(seed);
				user.setSecretKey(secretKey);
				System.out.println("before---------------");
				
				//实体类中更新了secretKey字段和seed字段后在数据库中刷新对应的数据
				userService.flush(user);
				
				System.out.println("after---------------");
				
				//放到页面的requestScope中
				map.put("secretKey",secretKey);
				map.put("user", user);
				return "user/first_totp";
			}
			//第一次注册成功后登录
			else {
				map.put("user",user);
				return "user/totp";
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
			@RequestParam(value="id",required=false) String id,Map<String, Object>map){
		//定位到数据表中的当前用户
		User user=userService.getByID(Integer.parseInt(id));
		//获取当前用户在服务器端的动态密码
		String serverCode=GoogleValidate.getValidateCode(user.getSeed());
		System.out.println("Server code--------------"+serverCode);
		System.out.println("param code--------------"+code);
		//验证成功
		if(serverCode.equals(code)){
			return "user/success";
		}
		//失败
		else{
			map.put("myError", "动态密码错误!");
			return "user/totp";
		}
	}
	
	//ajax请求验证用户名是否可用
	@ResponseBody
	@RequestMapping("/ajaxValidateName")
	public String validateName(@RequestParam(value="name",required=true) String name){
		User user=userService.getByName(name);
		//数据表中存在该用户，返回标记位 1 否则返回 0
		if(user==null)
			return "0";
		else return "1";
	}
	
	
}
