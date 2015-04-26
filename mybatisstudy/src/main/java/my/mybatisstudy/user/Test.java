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

		User user = new User();
		String id = "" + System.currentTimeMillis();
		user.setId(id);
		user.setUsername("user_" + id);
		user.setPassword("password_" + id);
		userService.add(user);

		user = userService.find(id);
		System.out.println(user);

		user.setUsername("test");
		user.setPassword("test");
		userService.update(user);
		user = userService.find(id);
		System.out.println(user);

		userService.delete(user.getId());
		user = userService.find(id);
		System.out.println(user);

		List<User> users = userService.list();
		System.out.println(users);
	}
}
