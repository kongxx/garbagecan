package my.httpstudy.okhttp;

import java.io.IOException;
import java.util.Map;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class HttpUtils {
	private static final String BASE_URL = "http://192.168.145.100:8000";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "Letmein";
	
	public static <T> T doGet(String path, ResponseHandler<T> responseHandler) throws IOException {
		return doGet(path, null, responseHandler);
	}

	public static <T> T doGet(String path, Map<String, Object> params, ResponseHandler<T> responseHandler) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String credential = Credentials.basic(USERNAME, PASSWORD);
		Request request = new Request.Builder().url(BASE_URL + path).header("Authorization", credential).build();
		Response response = client.newCall(request).execute();
		return doGet(response, responseHandler);
	}

	public static <T> T doGet(Response response, ResponseHandler<T> responseHandler) {
		T t = responseHandler.handle(response);
		return t;
	}
}
