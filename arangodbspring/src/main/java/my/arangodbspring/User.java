package my.arangodbspring;

import com.arangodb.springframework.annotation.Document;
import org.springframework.data.annotation.Id;

@Document("users")
public class User {
	@Id
	private String id;
	private String name;
	private int age;

	public User() {
		super();
	}

	public User(final String name, final int age) {
		super();
		this.name = name;
		this.age = age;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User: " + this.name;
	}
}
