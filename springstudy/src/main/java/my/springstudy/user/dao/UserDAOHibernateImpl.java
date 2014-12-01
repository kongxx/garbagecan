package my.springstudy.user.dao;

import my.springstudy.dao.GenericBaseDao;
import my.springstudy.user.model.User;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository ("userDAOHibernateImpl")
public class UserDAOHibernateImpl extends GenericBaseDao<User, String> implements UserDAO {

	@PostConstruct
	public void init() {
		this.entityClass = User.class;
	}

}
