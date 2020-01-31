package my.arangodbspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArangodbSpringApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ArangodbSpringApplication.class, args);
		final Class<?>[] runner = new Class<?>[] { UserRunner.class};
		System.exit(SpringApplication.exit(SpringApplication.run(runner, args)));
	}

}
