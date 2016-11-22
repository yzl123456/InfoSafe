package hznu.grade15x.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hznu.grade15x.entity.Employee;
import hznu.grade15x.repository.EmployeeRepository;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Transactional
	public Page<Employee> getPage(int pageNo,int pageSize){
		PageRequest pageRequest=new PageRequest(pageNo-1, pageSize);
		Page<Employee> page = employeeRepository.findAll(pageRequest);
		return page;		
	}
	
	@Transactional
	public Employee getByName(String name){
		Employee employee = employeeRepository.getByName(name);
		return employee;
	}
	
	@Transactional
	public void save(Employee employee){
		if(employee.getId()==null)
			employee.setCreateTime(new Date());
		employeeRepository.saveAndFlush(employee);
	}
	
	@Transactional
	public Employee get(Integer id){
		return employeeRepository.findOne(id);
	}
	
	@Transactional
	public void delete(Integer id){
		employeeRepository.delete(id);
	}
	
	
}
