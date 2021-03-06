package jurbanairship;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jurbanairship.notification.Notification;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Pusher {

	private static final Logger logger = LoggerFactory.getLogger(Pusher.class);

	private static final String PUSH_API = "https://go.urbanairship.com/api/push/";
	private static final String BATCH_PUSH_API = "https://go.urbanairship.com/api/push/batch/";

	public boolean push(Notification notification) throws PushException {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.setVersion(1.0)
				.create();
		String json = gson.toJson(notification);
		logger.debug("notification json: {}", json);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		Credentials credentials = new UsernamePasswordCredentials(Constants.username, Constants.password);
		httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);

		try {
			HttpPost post = new HttpPost(PUSH_API);
			post.setEntity(new JsonEntity(json));
			logger.debug("executing request: {}", post.getRequestLine());

			HttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String result = EntityUtils.toString(responseEntity);
				logger.debug("push result: {}", result);
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(result);
				if (jsonElement.getAsJsonObject().get("push_id") != null) {
					logger.debug("push id: {}", jsonElement.getAsJsonObject().get("push_id").getAsString());
				}
				return true;
			} else {
				logger.error("status code: {}", statusCode);
				return false;
			}
		} catch(IOException ex) {
			throw new PushException(ex.getMessage(), ex);
		}
	}

	public boolean batchPush(Notification ...notifications) throws PushException {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.setVersion(1.0)
				.create();
		String json = gson.toJson(notifications);
		logger.debug("notificationS json: {}", json);

		DefaultHttpClient httpClient = new DefaultHttpClient();
		Credentials credentials = new UsernamePasswordCredentials(Constants.username, Constants.password);
		httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);

		try {
			HttpPost post = new HttpPost(BATCH_PUSH_API);
			post.setEntity(new JsonEntity(json));
			logger.debug("executing request: {}", post.getRequestLine());

			HttpResponse response = httpClient.execute(post);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String result = EntityUtils.toString(responseEntity);
				logger.debug("push result: {}", result);
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonElement = jsonParser.parse(result);
				if (jsonElement.getAsJsonObject().get("push_id") != null) {
					logger.debug("push id: {}", jsonElement.getAsJsonObject().get("push_id").getAsString());
				}
				return true;
			} else {
				logger.error("status code: {}", statusCode);
				return false;
			}
		} catch(IOException ex) {
			throw new PushException(ex.getMessage(), ex);
		}
	}
}
