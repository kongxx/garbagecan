package my.springstudy.hibernate;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="DEPARTMENT")
public class Department implements Serializable {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name", length = 32, nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name="parent_id")
	private Department parent;

	@OneToMany(cascade = {CascadeType.ALL},
			mappedBy = "parent", fetch = FetchType.EAGER)
	private Set<Department> departments = new HashSet<Department>();

	@OneToMany(cascade = {CascadeType.ALL},
			mappedBy = "department", fetch = FetchType.EAGER)
	private Set<User> users = new HashSet<User>();

	public Department() {

	}

	public Department(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public void addDepartment(Department department){
		department.setParent(this);
		this.departments.add(department);
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addUser(User user){
		user.setDepartment(this);
		this.users.add(user);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
