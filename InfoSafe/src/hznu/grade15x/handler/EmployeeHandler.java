package hznu.grade15x.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hznu.grade15x.entity.Employee;
import hznu.grade15x.service.DepartmentService;
import hznu.grade15x.service.EmployeeService;

@Controller
public class EmployeeHandler {
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private DepartmentService departmentService;
	
	@ModelAttribute
	public void getEmployee(@RequestParam(value="id",required=false) Integer id
			,Map<String, Object>map){
		if(id!=null){
			Employee employee=employeeService.get(id);
			employee.setDepartment(null);
			map.put("employee",employee);
		}
//		else map.put("employee", new Employee());
		
	}
	
	
	@RequestMapping("/emps")
	public String list(@RequestParam(value="pageNo",defaultValue="1",required=false) String pageNoStr
			,Map<String,Object>map){
		int pageNo=Integer.parseInt(pageNoStr);
		Page<Employee> page = employeeService.getPage(pageNo, 5);
		map.put("page", page);
		return "emp/list";
	}
	
	@ResponseBody
	@RequestMapping("/ajaxValidateName")
	public String validateName(@RequestParam(value="name",required=true) String name){
		Employee employee=employeeService.getByName(name);
		if(employee==null)
			return "0";
		else return "1";
	}
	
	@RequestMapping(value="/emp",method=RequestMethod.GET)
	public String getAll(Map<String, Object>map){
		map.put("departments", departmentService.getAll());
		map.put("employee", new Employee());
		return "emp/input";
	}
	
	@RequestMapping(value="/emp",method=RequestMethod.POST)//新增
	public String save(Employee employee){
		employeeService.save(employee);
		return "redirect:/emps";
	}
	
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)//查询
	public String input(@PathVariable("id") Integer id,Map<String, Object>map ){
		map.put("employee", employeeService.get(id));
		map.put("departments",departmentService.getAll());
		return "emp/input";
	}
	
	@RequestMapping(value="/emp/{id}",method=RequestMethod.PUT)//更新
	public String update(Employee employee){
		employeeService.save(employee);
		return "redirect:/emps";
	}
	
	@RequestMapping(value="/emp/{id}",method=RequestMethod.DELETE)//删除
	public String delete(@PathVariable("id") Integer id){
		employeeService.delete(id);
		return "redirect:/emps";
	}
	
	
}

