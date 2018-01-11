package com.example.mybatis.dao;

import java.util.List;

import com.example.mybatis.bean.Employee;

public interface EmployeeMapperDynamicSQL {
    
	//携带了那个字段查询条件就带上这个字段
	public List<Employee> getEmpsByComditionIf(Employee employee);
}
