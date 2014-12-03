package my.springstudy.hibernate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UserServiceTest {

	private static DepartmentService departmentService;
	private static UserService userService;

	@BeforeClass
	public void beforeClass() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring.xml");
		departmentService = (DepartmentService) ctx.getBean("departmentService");
		userService = (UserService) ctx.getBean("userService");
	}

	@BeforeMethod
	public void setUp() throws Exception {

	}

	@AfterMethod
	public void tearDown() throws Exception {

	}

	@Test
	public void testAdd() throws Exception {
		Department department = new Department("0", "root");
		departmentService.add(department);
		department = departmentService.findById("0");

		User user = new User("0", "admin");
		user.setDepartment(department);
		userService.add(user);
		userService.deleteById("0");
	}

//	@Test
//	public void testDeleteById() throws Exception {
//
//	}
//
//	@Test
//	public void testFindById() throws Exception {
//
//	}
}