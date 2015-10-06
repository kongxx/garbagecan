package my.httpstudy.jersey;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jsonp.JsonProcessingFeature;

import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

public class HttpClient {
	private static final String BASE_URL = "http://127.0.0.1:8000";
	private static final String username = "admin";
	private static final String password = "Letmein";

	public <T> T execute(String path, ResponseHandler<T> responseHandler) {
		return execute(path, null, responseHandler);
	}

	public <T> T execute(String path, Map<String, Object> params, ResponseHandler<T> responseHandler) {
		Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
		client.register(feature);
		client.register(JsonProcessingFeature.class).property(JsonGenerator.PRETTY_PRINTING, true);
		WebTarget webTarget = client.target(BASE_URL + path);
		if (params != null) {
			for (String key : params.keySet()) {
				webTarget = webTarget.queryParam(key, params.get(key));
			}
		}
		Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
		T t = execute(response, responseHandler);
		client.close();
		return t;
	}

	public <T> T execute(Response response, ResponseHandler<T> responseHandler) {
		T t = responseHandler.handle(response);
		return t;
	}
}
