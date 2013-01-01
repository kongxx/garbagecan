package jurbanairship;

import jurbanairship.device.Device;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Register {

	private static final Logger logger = LoggerFactory.getLogger(Register.class);

	private static final String API_PATH = "https://go.urbanairship.com/api/apids/";

	public boolean register(Device device) throws RegisterException {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"alias\": \"example_alias\", \"tags\": [\"tag11\", \"tag22\"]}");
		String path = API_PATH + device.getId();

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(Constants.username, Constants.password));

			HttpPut httpPut = new HttpPut(path);
			httpPut.setHeader("Accept", "application/json");
			httpPut.setEntity(new JsonEntity(sb.toString()));
			logger.debug("register request: " + httpPut.getRequestLine());

			HttpResponse response = httpClient.execute(httpPut);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
//				HttpEntity responseEntity = response.getEntity();
//				String result = EntityUtils.toString(responseEntity);
//				return result.equals("OK");
				return true;
			} else {
				return false;
			}
		} catch(IOException ex) {
			throw new RegisterException(ex.getMessage(), ex);
		}
	}

}
