import my.springstudy.user.model.User;
import my.springstudy.user.service.MyUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring.xml");
		MyUserService userService = (MyUserService) ctx.getBean("myUserService");
		User user = new User();
		user.setUsername("4");
		user.setPassword("4");
		user = userService.add(user);
		System.out.println(user);
	}
}
