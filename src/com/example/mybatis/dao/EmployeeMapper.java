package com.example.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.example.mybatis.bean.Employee;

public interface EmployeeMapper {
	public Employee getEmpByMap(Map<String, Object> map);

	public List<Employee> getEmpsByLastNameLike(String lastName);

	public Map<String, Object> getEmpByReturnMap(Integer id);
	//告诉MyBatis封装map时用那个属性作为map的key
	@MapKey("lastName")
	public Map<String, Employee> getEmpsByLastNameLikeReturnMap(String lastName);

	public Employee getEmpByIdAndName(@Param("id") Integer id, @Param("lastName") String lastName);

	public Employee getEmpById(Integer id);

	public void addEmp(Employee employee);

	public void updateEmp(Employee employee);

	public Long deleteEmp(Integer id);
}
