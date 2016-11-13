package hznu.grade15x.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hznu.grade15x.entity.User;
//Repository仓库类，通过JPA自动提供的CRUD（增删改查）方法
public interface UserRepository extends JpaRepository<User, Integer>{
	//通过名字得到一个对象
	public User getByUsername(String name);
	//通过ID得到一个对象
	public User getById(Integer id);
}
