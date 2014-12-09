package my.springstudy.user.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity (name = "t_user")
@Table(name="t_user")
public class User implements Serializable {

	@Id
	@Column(name = "id")
	@GenericGenerator(name = "UUID", strategy = "uuid")
	@GeneratedValue(generator = "UUID")
	private String id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
