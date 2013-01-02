package jurbanairship;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jurbanairship.device.Device;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;

public class Register {

	private static final Logger logger = LoggerFactory.getLogger(Register.class);

	private static final String BASE_API_PATH = "https://go.urbanairship.com";

	public boolean register(Device device) throws RegisterException {
		Gson gson = new GsonBuilder()
				.serializeNulls()
				.setDateFormat(DateFormat.LONG)
				.setPrettyPrinting()
				.setVersion(1.0)
				.create();
		String json = gson.toJson(device);
		logger.debug("device json: {}", json);

		String path = BASE_API_PATH + device.getRegisterAPIPath() + device.getId();
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
				json = getDevice(device);
				if (json != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch(IOException ex) {
			throw new RegisterException(ex.getMessage(), ex);
		}
	}

	public boolean unregister(Device device) throws RegisterException {
		String path = BASE_API_PATH + device.getRegisterAPIPath() + device.getId();
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			Credentials credentials = new UsernamePasswordCredentials(Constants.username, Constants.password);
			httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);

			HttpDelete httpDelete = new HttpDelete(path);
			httpDelete.setHeader(new BasicHeader("Content-Type", "application/json"));
			logger.debug("register request: {}", httpDelete.getRequestLine());

			HttpResponse response = httpClient.execute(httpDelete);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (statusCode == HttpStatus.SC_NO_CONTENT) {
				String json = getDevice(device);
				if (json != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch(IOException ex) {
			throw new RegisterException(ex.getMessage(), ex);
		}
	}

	private String getDevice(Device device) throws RegisterException {
		String path = BASE_API_PATH + device.getRegisterAPIPath() + device.getId();

		DefaultHttpClient httpClient = new DefaultHttpClient();
		Credentials credentials = new UsernamePasswordCredentials(Constants.username, Constants.password);
		httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);

		try {
			HttpGet httpGet = new HttpGet(path);
			httpGet.setHeader(new BasicHeader("Content-Type", "application/json"));

			HttpResponse response = httpClient.execute(httpGet);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String result = EntityUtils.toString(responseEntity);
				logger.debug("Device info from server: {}", result);
				return result;
			} else {
				return null;
			}
		} catch (IOException ex) {
			throw new RegisterException(ex.getMessage(), ex);
		}
	}
}
