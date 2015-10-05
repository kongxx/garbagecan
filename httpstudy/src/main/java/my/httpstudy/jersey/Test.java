package my.httpstudy.jersey;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jsonp.JsonProcessingFeature;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class Test {
	private static final String BASE_URL = "http://127.0.0.1:8000";
	private static final String username = "admin";
	private static final String password = "Letmein";

	public static void main(String[] args) throws Exception {
		testGet("/articles/api/articles/");
		testGet("/articles/api/articles/1");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("article_id", 6);
		testGet("/articles/api/comments/", params);
	}

	private static void testGet(String path) {
		Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
		client.register(feature);
		client.register(JsonProcessingFeature.class).property(JsonGenerator.PRETTY_PRINTING, true);
		Response response = client.target(BASE_URL + path).request(MediaType.APPLICATION_JSON).get();
		System.out.println(response.getStatus());
		JsonObject jsonObject = response.readEntity(JsonObject.class);
		System.out.println(jsonObject);
//		System.out.println(jsonObject.getJsonArray("results").getJsonObject(0).getString("title"));
		JsonArray jsonArray = jsonObject.getJsonArray("results");
		if (jsonArray != null) {
			int length = jsonArray.size();
			for (int i = 0; i < length; i++) {
				System.out.println(jsonArray.getJsonObject(i));
			}
		}
		client.close();
	}

	private static void testGet(String path, Map<String, Object> params) {
		Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
		client.register(feature);
		client.register(JsonProcessingFeature.class).property(JsonGenerator.PRETTY_PRINTING, true);
		WebTarget webTarget = client.target(BASE_URL + path);
		for (String key : params.keySet()) {
			webTarget = webTarget.queryParam(key, params.get(key));
		}
		Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
		System.out.println(response.getStatus());
		JsonObject jsonObject = response.readEntity(JsonObject.class);
//		System.out.println(jsonObject);
//		System.out.println(jsonObject.getJsonArray("results").getJsonObject(0).getString("title"));
		JsonArray jsonArray = jsonObject.getJsonArray("results");
		if (jsonArray != null) {
			int length = jsonArray.size();
			for (int i = 0; i < length; i++) {
				System.out.println(jsonArray.getJsonObject(i));
			}
		}
		client.close();
	}
}
