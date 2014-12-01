package my.springstudy.user.dao;

import my.springstudy.user.model.User;

public interface UserDAO {
	public User findById(String id);
}

