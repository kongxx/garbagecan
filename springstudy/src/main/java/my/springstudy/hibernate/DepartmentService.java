package my.springstudy.hibernate;

import my.springstudy.user.dao.UserDAO;
import my.springstudy.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentDAO departmentDAO;

	@Transactional
	public void add(Department department) {
		departmentDAO.save(department);
	}

	@Transactional
	public Department findById(String id) {
		return departmentDAO.get(id);
	}
}
