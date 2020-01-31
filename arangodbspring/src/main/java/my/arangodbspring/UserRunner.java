package my.arangodbspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;

//import com.arangodb.spring.demo.entity.Character;
//import com.arangodb.spring.demo.repository.CharacterRepository;
import com.arangodb.springframework.core.ArangoOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@ComponentScan("my.arangodbspring")
public class UserRunner implements CommandLineRunner {
	@Autowired
	private ArangoOperations operations;
	@Autowired
	private UserRepository repository;

	@Override
	public void run(final String... args) throws Exception {
		operations.dropDatabase();

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
	}

}
