package my.arangodbspring;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;

public interface UserRepository extends ArangoRepository<User, String> {
	Iterable<User> findByName(String name);

	@Query("FOR user IN users FILTER user.age > @age SORT user.age DESC RETURN user")
	Iterable<User> getOlderThan(int age);
}
