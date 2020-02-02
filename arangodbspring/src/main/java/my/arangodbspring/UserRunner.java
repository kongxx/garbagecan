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
