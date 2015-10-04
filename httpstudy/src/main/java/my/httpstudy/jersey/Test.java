package my.httpstudy.jersey;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jsonp.JsonProcessingFeature;

import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Test {
	public static void main(String[] args) throws Exception {
		Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("admin", "Letmein");
		client.register(feature);
		client.register(JsonProcessingFeature.class).property(JsonGenerator.PRETTY_PRINTING, true);
		Response response = client.target("http://127.0.0.1:8000/articles/api/articles/").request(MediaType.APPLICATION_JSON).get();
		System.out.println(response.getStatus());
		JsonObject jsonObject = response.readEntity(JsonObject.class);
		System.out.println(jsonObject.getJsonArray("results").getJsonObject(0).getString("title"));
	}
}
