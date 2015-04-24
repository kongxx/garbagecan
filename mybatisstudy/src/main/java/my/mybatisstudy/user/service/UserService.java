package my.mybatisstudy.user.service;

import my.mybatisstudy.user.mapper.UserMapper;
import my.mybatisstudy.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service ("userService")
@Scope("prototype")
public class UserService {
	@Autowired
	private UserMapper userMapper;

	public List<User> list() {
		return userMapper.list();
	}

	public User find(String id) {
		return userMapper.find(id);
	}
}
