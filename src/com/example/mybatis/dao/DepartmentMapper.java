package com.example.mybatis.dao;

import com.example.mybatis.bean.Department;

public interface DepartmentMapper {

	public Department getDeptById(Integer id);

	public Department getDeptByIdPlus(Integer id);

	public Department getDeptByIdStep(Integer id);
}
