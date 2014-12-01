package my.springstudy.user.service;

import my.springstudy.user.dao.UserDAO;
import my.springstudy.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class UserService {
	@Autowired
	@Qualifier ("userDAOJDBCImpl")
	private UserDAO userDAOJDBCImpl;

	@Autowired
	@Qualifier ("userDAOJDBCImpl")
	private UserDAO userDAOHibernateImpl;

	public User find(String id) {
		System.out.println("JDBC: " + userDAOJDBCImpl.findById(id));
		System.out.println("Hibernate: " + userDAOHibernateImpl.findById(id));
		return userDAOHibernateImpl.findById(id);
	}
}
