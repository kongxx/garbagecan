package my.springstudy.user.controller;

import my.springstudy.user.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

	@Autowired
	private MyUserService userService;
	
	@RequestMapping("/user/list")
	public String list(Model model) {
//		model.addAttribute("users", name);
//		System.out.println(userService.find("1"));
		return "list";
	}
}
