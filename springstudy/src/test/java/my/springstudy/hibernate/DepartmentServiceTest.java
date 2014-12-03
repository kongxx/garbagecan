package my.springstudy.hibernate;

import my.springstudy.user.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DepartmentServiceTest {

	private static DepartmentService departmentService;

	@BeforeClass
	public void beforeClass() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring.xml");
		departmentService = (DepartmentService) ctx.getBean("departmentService");
	}

	@BeforeMethod
	public void setUp() throws Exception {

	}

	@AfterMethod
	public void tearDown() throws Exception {

	}

	@Test
	public void testAddRoot() throws Exception {
		Department department = new Department("0", "root");
		departmentService.add(department);
		department = departmentService.findById("0");
		assertNotNull(department);
		assertEquals("root", department.getName());
	}

	@Test
	public void testAddChild() throws Exception {
		Department parent = new Department("0", "root");
		Department child1 = new Department("1", "child1");
		Department child11 = new Department("11", "child11");
		child1.addChild(child11);
		parent.addChild(child1);
		departmentService.add(parent);

		Department department = departmentService.findById("0");
		assertNotNull(department);
		assertEquals("root", department.getName());
		assertEquals(1, department.getChildren().size());
		assertEquals("child1", department.getChildren().iterator().next().getName());
		assertEquals("child11", department.getChildren().iterator().next().getChildren().iterator().next().getName());

		department = departmentService.findById("1");
		assertNotNull(department);
		assertEquals("child1", department.getName());
		assertEquals("root", department.getParent().getName());

		department = departmentService.findById("11");
		assertNotNull(department);
		assertEquals("child11", department.getName());
		assertEquals("child1", department.getParent().getName());
	}

	@org.testng.annotations.Test
	public void testFindById() throws Exception {

	}
}