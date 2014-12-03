package my.springstudy.hibernate;

import my.springstudy.dao.GenericBaseDao;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class DepartmentDAO extends GenericBaseDao<Department, String> {
	@PostConstruct
	public void init() {
		this.entityClass = Department.class;
	}
}
