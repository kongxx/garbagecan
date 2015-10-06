package my.httpstudy.jersey;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	public static void main(String[] args) throws Exception {
		testGetList("/articles/api/articles/");
		testGet("/articles/api/articles/1");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("article_id", 6);
		testGetList("/articles/api/comments/", params);
	}

	private static void testGetList(String path) {
		testGetList(path, null);
	}

	private static void testGetList(String path, Map<String, Object> params) {
		new HttpClient().execute("/articles/api/articles/", params, new ResponseHandler<List<JsonObject>>() {
			public List<JsonObject> handle(Response response) throws RuntimeException {
				System.out.println(response.getStatus());
				List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
				JsonArray jsonArray = response.readEntity(JsonObject.class).getJsonArray("results");
				if (jsonArray != null) {
					int length = jsonArray.size();
					for (int i = 0; i < length; i++) {
						jsonObjects.add(jsonArray.getJsonObject(i));
					}
				}
				System.out.println(jsonObjects);
				return jsonObjects;
			}
		});
	}

	private static void testGet(String path) {
		testGet(path, null);
	}

	private static void testGet(String path, Map<String, Object> params) {
		new HttpClient().execute("/articles/api/articles/", params, new ResponseHandler<JsonObject>() {
			public JsonObject handle(Response response) throws RuntimeException {
				System.out.println(response.getStatus());
				JsonObject jsonObject = response.readEntity(JsonObject.class);
				System.out.println(jsonObject);
				return jsonObject;
			}
		});
	}
}
