package my.springstudy.user.dao;

import my.springstudy.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

@Repository ("userDAOHibernateImpl")
public class UserDAOHibernateImpl implements UserDAO {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public User findById(String id) {
		return hibernateTemplate.get(User.class, id);
	}
}
