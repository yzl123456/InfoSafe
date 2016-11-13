package hznu.grade15x.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hznu.grade15x.entity.User;
import hznu.grade15x.repository.UserRepository;
//service层
@Service
public class UserService {
	//自动装配
	@Autowired
	private UserRepository userRepository;
	
	//添加事务，这里是用来做分页的，可用直接忽略，用来将数据库的信息显示在前台页面
	@Transactional
	public Page<User> getPage(int pageNo,int pageSize){
		PageRequest pageRequest=new PageRequest(pageNo-1, pageSize);
		Page<User> page = userRepository.findAll(pageRequest);
		return page;		
	}
	
	//在数据库保存一个对象
	@Transactional
	public void save(User user){
		userRepository.saveAndFlush(user);
	}
	
	//通过username得到一个对象
	public User getByName(String username){
		return userRepository.getByUsername(username);
	}
	
	//通过ID得到一个对象
	public User getByID(Integer id){
		return userRepository.getById(id);
	}
	
	//讲已存在的实体类修改后刷新到数据库，使数据库的对象与实体类一致
	public void flush(User user){
		userRepository.saveAndFlush(user);
	}
}
