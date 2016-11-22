package hznu.grade15x.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hznu.grade15x.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

	public Employee getByName(String name);
}
