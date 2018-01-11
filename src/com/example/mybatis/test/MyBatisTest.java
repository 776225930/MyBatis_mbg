package com.example.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.mybatis.bean.Department;
import com.example.mybatis.bean.Employee;
import com.example.mybatis.dao.DepartmentMapper;
import com.example.mybatis.dao.EmployeeMapper;
import com.example.mybatis.dao.EmployeeMapperAnnotation;
import com.example.mybatis.dao.EmployeeMapperDynamicSQL;
import com.example.mybatis.dao.EmployeeMapperPlus;

/**
 * 1、接口式编程 原生： Dao ====> DaoImpl mybatis： Mapper ====> xxMapper.xml
 * 
 * 2、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 3、SqlSession和connection一样都是非线程安全。每次使用都应该去获取新的对象。
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。 （将接口和xml进行绑定） EmployeeMapper
 * empMapper = sqlSession.getMapper(EmployeeMapper.class); 5、两个重要的配置文件：
 * mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息 sql映射文件：保存了每一个sql语句的映射信息：
 * 将sql抽取出来。
 * 
 * @author JHao
 *
 */
public class MyBatisTest {
	private SqlSessionFactory sessionFactory = null;
	// 非线程安全。这样写不对,仅在此这么用
	private SqlSession session = null;

	@Before
	public void init() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream in = Resources.getResourceAsStream(resource);
		sessionFactory = new SqlSessionFactoryBuilder().build(in);
		session = sessionFactory.openSession();
	}

	@After
	public void destroy() {
		session.commit();
		session.close();
	}

	/**
	 * 两级缓存: 一级缓存:(本地缓存)sqlSession级别的缓存,一级缓存是一只开启的
	 *     与数据库同一次会话期间的数据会放在本地缓存中
	 *     以后如果要获取相同的数据,直接从缓存中去拿没必要再查询数据库
	 * 
	 */
	@Test
	public void testFirstLevelCache() {
		EmployeeMapper mapper=session.getMapper(EmployeeMapper.class);
		Employee emp1=mapper.getEmpById(1);
		Employee emp2=mapper.getEmpById(1);
		System.out.println(emp1==emp2);
		System.out.println(emp1);
	}
	@Test
	public void testSecondLevelCache() {
		SqlSession session=sessionFactory.openSession();
		SqlSession session2=sessionFactory.openSession();
		EmployeeMapper mapper=session.getMapper(EmployeeMapper.class);
		Employee emp1=mapper.getEmpById(1);
		System.out.println(emp1);
		session.close();
		
		mapper=session2.getMapper(EmployeeMapper.class);
		Employee emp2=mapper.getEmpById(1);
		System.out.println(emp2);
		session2.close();
	}

}
