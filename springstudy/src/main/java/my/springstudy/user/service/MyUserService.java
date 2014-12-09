package my.springstudy.user.service;

import my.springstudy.user.dao.UserDAO;
import my.springstudy.user.dao.UserDAOHibernateImpl;
import my.springstudy.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.annotation.Resources;

@Service
public class MyUserService {
//	@Autowired
//	@Qualifier ("userDAOJDBCImpl")
//	private UserDAO userDAOJDBCImpl;
//
//	@Autowired
//	@Qualifier ("userDAOHibernateImpl")
//	private UserDAO userDAOHibernateImpl;

	@Autowired
	@Qualifier ("userDAOHibernateImpl")
	private UserDAO userDAO;

	@Transactional
	public User add(User user) {
		return userDAO.save(user);
	}

	@Transactional
	public User findById(String id) {
		return userDAO.get(id);
	}
}
