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
				.host("106.54.228.237", 8529)
				.user("root")
				.password("Letmein");
		return arango;
	}

	@Override
	public String database() {
		return "mydb";
	}

}