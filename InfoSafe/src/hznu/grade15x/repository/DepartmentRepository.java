package hznu.grade15x.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import hznu.grade15x.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>{
	@QueryHints({@QueryHint(name=org.hibernate.ejb.QueryHints.HINT_CACHEABLE,value="true")})
	@Query("from Department d")
	public List<Department> getAll();

}
