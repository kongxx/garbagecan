package my.springstudy.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Transactional
	public void add(User user) {
		userDAO.save(user);
	}

	@Transactional
	public void deleteById(String id) {
		userDAO.deleteByKey(id);
	}

	public User findById(String id) {
		return userDAO.get(id);
	}
}
