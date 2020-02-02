# ArangoDB与SpringData集成

今天看看 ArangoDB 怎样与 Spring Data 集成使用。

## 创建工程
首先访问 https://start.spring.io 创建一个 spring-boot 项目。然后添加如下依赖

``` shell
		<dependency>
			<groupId>com.arangodb</groupId>
			<artifactId>arangodb-spring-data</artifactId>
			<version>3.2.3</version>
		</dependency>

		<dependency>
			<groupId>org.apache.httpcomponents</groupId>
			<artifactId>httpclient</artifactId>
			<version>4.5.1</version>
		</dependency>
```

完整的 pom.xml 文件如下：

``` shell
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.2.4.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>my</groupId>
	<artifactId>arangodbspring</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>arangodbspring</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>

		<dependency>
			<groupId>com.arangodb</groupId>
			<artifactId>arangodb-spring-data</artifactId>
			<version>3.2.3</version>
		</dependency>

		<dependency>
			<groupId>org.apache.httpcomponents</groupId>
			<artifactId>httpclient</artifactId>
			<version>4.5.1</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
```

## 代码实例

### 实体类 User.java

``` shell
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
```

### 实体对象库类 UserRepository.java

增加了两个方法，一个是按用户名称查找，一个是使用AQL查询。

``` shell
package my.arangodbspring;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;

public interface UserRepository extends ArangoRepository<User, String> {
	Iterable<User> findByName(String name);

	@Query("FOR user IN users FILTER user.age > @age SORT user.age DESC RETURN user")
	Iterable<User> getOlderThan(int age);
}
```

### 测试类 UserRunner.java

其中包括了使用 ArangoOperations 和 UserRepository 操作集合的方式。

``` shell
package my.arangodbspring;

import com.arangodb.ArangoCursor;
import com.arangodb.springframework.core.CollectionOperations;
import com.arangodb.util.MapBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

import com.arangodb.springframework.core.ArangoOperations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Map;

@ComponentScan("my.arangodbspring")
public class UserRunner implements CommandLineRunner {
	@Autowired
	private ArangoOperations operations;
	@Autowired
	private UserRepository repository;

	@Override
	public void run(final String... args) throws Exception {
		operations.dropDatabase();

		System.out.println("Test for UserRepository ...");

		for (int i = 0; i < 10; i++) {
			final User user = new User("user_" + i, 20 + i);
			repository.save(user);
		}
		repository.findAll().forEach(System.out::println);

		repository.findAll(Sort.by(Sort.Direction.ASC, "name"))
				.forEach(System.out::println);

		repository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name")))
				.forEach(System.out::println);

		System.out.println(repository.findByName("user_5"));

		System.out.println(repository.getOlderThan(15));

		System.out.println("Test for ArangoOperations ...");

		CollectionOperations collectionOperations = operations.collection("users");
		System.out.println("users count: " + collectionOperations.count());

		Iterable<User> users = operations.findAll(User.class);
		for (User user: users) {
			System.out.println("user: " + user);
		}

		String query = "FOR user IN users" +
				" FILTER user.name == @name || user.age >= @age " +
				" LIMIT 2, 5 " +
				" RETURN user";
		Map<String, Object> params = new MapBuilder()
				.put("name", "user_0")
				.put("age", 25)
				.get();
		ArangoCursor<User> cursor = operations.query(query, params, null, User.class);
		cursor.forEachRemaining(user -> {
			System.out.println("user: " + user);
		});
	}

}
```

### 配置类 MyConfiguration.java

配置 ArangoDB 的机器，端口，用户名和密码等，这里是直接在代码里写了，也可以通过配置文件传入。

``` shell
package my.arangodbspring;

import com.arangodb.ArangoDB;
import com.arangodb.Protocol;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.AbstractArangoConfiguration;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = {"my.arangodbspring"})
public class MyConfiguration extends AbstractArangoConfiguration  {

	@Override
	public ArangoDB.Builder arango() {
		ArangoDB.Builder arango = new ArangoDB.Builder()
				.useProtocol(Protocol.HTTP_JSON)
				.host("localhost", 8529)
				.user("root")
				.password("<password>");
		return arango;
	}

	@Override
	public String database() {
		return "mydb";
	}

}
```

### 应用启动类 ArangodbSpringApplication.java

``` shell
package my.arangodbspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArangodbSpringApplication {

	public static void main(String[] args) {
		final Class<?>[] runner = new Class<?>[] { UserRunner.class};
		System.exit(SpringApplication.exit(SpringApplication.run(runner, args)));
	}

}
```
