package my.springstudy.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void deleteById(String id) {
		Department department = departmentDAO.get(id);
		if (department != null) {
			if (department.getParent() != null) {
				department.getParent().getDepartments().remove(department);
				department.setParent(null);
			}
			departmentDAO.delete(department);
		}
	}

	public Department findById(String id) {
		return departmentDAO.get(id);
	}
}
