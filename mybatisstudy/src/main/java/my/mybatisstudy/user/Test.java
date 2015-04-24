package my.mybatisstudy.user;

import my.mybatisstudy.user.model.User;
import my.mybatisstudy.user.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class Test {
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring.xml");
		UserService userService = (UserService) ctx.getBean("userService");
//		User user = new User();
//		user.setUsername("4");
//		user.setPassword("4");
//		user = userService.add(user);
//		System.out.println(user);

//		User user = userService.find("1");
//		System.out.println(user);

		List<User> users = userService.list();
		System.out.println(users);
	}
}
