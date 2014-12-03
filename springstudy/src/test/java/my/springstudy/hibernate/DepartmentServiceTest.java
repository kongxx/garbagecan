package my.springstudy.hibernate;

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
		Department department = departmentService.findById("0");
		if (department != null) {
			departmentService.deleteById("0");
		}
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
	public void testAdd() throws Exception {
		Department department = new Department("0", "root");
		Department subDepartment1 = new Department("1", "subDepartment1");
		Department subDepartment2 = new Department("2", "subDepartment2");
		department.addDepartment(subDepartment1);
		department.addDepartment(subDepartment2);
		department.addUser(new User("0", "admin"));
		subDepartment1.addUser(new User("1", "user1"));
		subDepartment2.addUser(new User("2", "user2"));
		departmentService.add(department);

		department = departmentService.findById("0");
		assertNotNull(department);
		assertEquals("root", department.getName());
		assertEquals(2, department.getDepartments().size());
		assertEquals(1, department.getUsers().size());

		department = departmentService.findById("1");
		assertNotNull(department);
		assertEquals("subDepartment1", department.getName());
		assertEquals("root", department.getParent().getName());
		assertEquals(1, department.getUsers().size());

		department = departmentService.findById("2");
		assertNotNull(department);
		assertEquals("subDepartment2", department.getName());
		assertEquals("root", department.getParent().getName());
		assertEquals(1, department.getUsers().size());
	}

	@Test
	public void testDeleteById() throws Exception {
		Department department = new Department("0", "root");
		department.addDepartment(new Department("1", "department"));
		department.addUser(new User("1", "user"));
		departmentService.add(department);
		department = departmentService.findById("0");
		assertNotNull(department);
		assertEquals(1, department.getUsers().size());
		assertEquals(1, department.getDepartments().size());

		departmentService.deleteById("0");
		department = departmentService.findById("0");
		assertNull(department);

		department = new Department("0", "root");
		department.addDepartment(new Department("1", "department"));
		departmentService.add(department);
		department = departmentService.findById("1");
		assertNotNull(department);
		departmentService.deleteById("1");
		department = departmentService.findById("1");
		assertNull(department);

	}

	@Test
	public void testFindById() throws Exception {
		Department department = null;

		department = departmentService.findById("0");
		assertNull(department);

		department = new Department("0", "root");
		departmentService.add(department);
		department = departmentService.findById("0");
		assertNotNull(department);
		assertEquals("root", department.getName());
	}
}