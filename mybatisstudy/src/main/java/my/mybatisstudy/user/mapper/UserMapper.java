package my.mybatisstudy.user.mapper;

import my.mybatisstudy.user.model.User;

import java.util.List;

public interface UserMapper {

	List<User> list();

	User find(String id);

	void add(User user);

	void update(User user);

	void delete(String id);

}
