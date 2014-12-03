package my.springstudy.hibernate;

import my.springstudy.dao.GenericBaseDao;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class UserDAO extends GenericBaseDao<User, String> {
	@PostConstruct
	public void init() {
		this.entityClass = User.class;
	}
}
