import my.springstudy.user.model.User;
import my.springstudy.user.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring.xml");
		UserService userService = (UserService) ctx.getBean("userService");
		System.out.println(userService.findById("1"));
		User user = new User();
		user.setId("3");
		user.setUsername("3");
		user.setPassword("3");
		userService.add(user);
		System.out.println(userService.findById("3"));
	}
}
