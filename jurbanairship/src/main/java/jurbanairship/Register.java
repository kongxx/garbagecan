package jurbanairship;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jurbanairship.device.Device;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;

public class Register {

	private static final Logger logger = LoggerFactory.getLogger(Register.class);

	private static final String API_PATH = "https://go.urbanairship.com/api/apids/";

	public boolean register(Device device) throws RegisterException {
		Gson gson = new GsonBuilder()
				.serializeNulls()
				.setDateFormat(DateFormat.LONG)
				.setPrettyPrinting()
				.setVersion(1.0)
				.create();
		String json = gson.toJson(device);
		logger.debug("device json: {}", json);

		String path = API_PATH + device.getId();
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			Credentials credentials = new UsernamePasswordCredentials(Constants.username, Constants.password);
			httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);

			HttpPut httpPut = new HttpPut(path);
			httpPut.setEntity(new JsonEntity(json));
			logger.debug("register request: {}", httpPut.getRequestLine());

			HttpResponse response = httpClient.execute(httpPut);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return true;
			} else {
				return false;
			}
		} catch(IOException ex) {
			throw new RegisterException(ex.getMessage(), ex);
		}
	}

}
