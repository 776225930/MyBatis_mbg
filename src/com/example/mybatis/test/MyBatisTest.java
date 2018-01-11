package com.example.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
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
	 * 1.根据xml文件(全局配置文件)创建一个SqlSessionFactory 2、sql映射文件；配置了每一个sql，以及sql的封装规则等。
	 * 3、将sql映射文件注册在全局配置文件中 4、写代码： 1）、根据全局配置文件得到SqlSessionFactory；
	 * 2）、使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
	 * 一个sqlSession就是代表和数据库的一次会话，用完关闭
	 * 3）、使用sql的唯一标志来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件中的。
	 * 
	 * @throws IOException
	 */
	@Test 
	public void testOracle() throws IOException{
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		// 获取SqlSession实例,能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		session.selectOne("getEmpById", 1);
	}
	@Test
	public void test() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		// 获取SqlSession实例,能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		try {
			Employee emp = session.selectOne("selectEmp", 1);
			System.out.println(emp);
		} finally {
			//session.close();
		}
	}
    
	@Test
	public void test1() throws IOException {
		// 3、获取接口的实现类对象
		// 会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

		Employee employee = mapper.getEmpById(7844);
		System.out.println(mapper.getClass());
		System.out.println(employee);
	}

	@Test
	public void test2() {
		EmployeeMapperAnnotation mapper = session.getMapper(EmployeeMapperAnnotation.class);
		Employee employee = mapper.getEmpById(1);
		System.out.println(mapper.getClass());
		System.out.println(employee);
	}
	/**
	 * 测试增删改
	 * 1、mybatis允许增删改直接定义以下类型返回值
	 * 		Integer、Long、Boolean、void
	 * 2、需要手动提交数据
	 * 		sqlSessionFactory.openSession();===》手动提交
	 * 		sqlSessionFactory.openSession(true);===》自动提交
	 * @throws IOException 
	 */
	@Test
	public void test3() {
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		//测试添加
		Employee employee=new Employee(null, "Jerry", "jerry@163.com", "0");
		mapper.addEmp(employee);
		System.out.println(employee.getId());
	
		//测试更新
		//Employee employee=new Employee(1, "Jerry", "jerry@163.com", "0");
		//mapper.updateEmp(employee);
		
		 //删除
		//mapper.deleteEmp(2);
		System.out.println("has commited");
	}
	@Test
	public void test4() {
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Map<String, Object> map=new HashMap<>();
		map.put("id", 1);
		map.put("lastName", "Jerry");
		Employee employee=mapper.getEmpByMap(map);
		System.out.println(employee);
	}
	@Test
	public void test5() {
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		List<Employee> employees=mapper.getEmpsByLastNameLike("%j%");
		System.out.println(employees);
	}
	@Test
	public void test6() {
		EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
		Map<String, Employee> employees=mapper.getEmpsByLastNameLikeReturnMap("%j%");
		System.out.println(employees);
	}
	@Test
	public void test7() {
		EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
		Employee  employee=mapper.getEmpById(1);
		System.out.println(employee);
	}
	@Test
	public void test8() {
		EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
		Employee  employeeAndDept=mapper.getEmpAndDept(1);
		System.out.println(employeeAndDept);
		System.out.println(employeeAndDept.getDept());
	}
	@Test
	public void testStep() {
		EmployeeMapperPlus mapper = session.getMapper(EmployeeMapperPlus.class);
		Employee  employeeAndDept=mapper.getEmpByIdStep(3);
		System.out.println(employeeAndDept);
		System.out.println(employeeAndDept.getDept());
	}
	@Test
	public void test9() {
		DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
		Department dept= mapper.getDeptByIdPlus(1);
		System.out.println(dept);
		System.out.println(dept.getEmps());
	}
	@Test
	public void test10() {
		DepartmentMapper mapper = session.getMapper(DepartmentMapper.class);
		Department dept= mapper.getDeptByIdStep(1);
		System.out.println(dept);
		System.out.println(dept.getEmps());
	}
	/*=================================================*/
	@Test
	public void testGetEmpsByComditionIf() {
		EmployeeMapperDynamicSQL mapper = session.getMapper(EmployeeMapperDynamicSQL.class);
		Employee employee=new Employee(null, "%e%",null, null);
		List<Employee> emps= mapper.getEmpsByComditionIf(employee);
		System.out.println(emps);
	}
	//查询时如果某些条件没有没带，sql语句可能会拼错
	//1.给where后面加上1=1,以后的条件都是and xxx
	//2.使用<where>将查询条件包括在里边
	@Test
	public void testGetEmpsByComditionTrim() {
		EmployeeMapperDynamicSQL mapper = session.getMapper(EmployeeMapperDynamicSQL.class);
		Employee employee=new Employee(null, "%e%",null, null);
		List<Employee> emps= mapper.getEmpsByComditionTrim(employee);
		System.out.println(emps);
	}
}
