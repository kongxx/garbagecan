package my.httpstudy.okhttp;

import java.io.IOException;
import java.util.Map;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class HttpUtils {
	private static final String BASE_URL = "http://192.168.145.100:8000";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "Letmein";
	
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	
	public static <T> T doGet(String path, ResponseHandler<T> responseHandler) throws ExecuteException {
		return doGet(path, null, responseHandler);
	}

	public static <T> T doGet(String path, Map<String, Object> params, ResponseHandler<T> responseHandler) throws ExecuteException {
		OkHttpClient client = new OkHttpClient();
		String credential = Credentials.basic(USERNAME, PASSWORD);
		Request.Builder builder = new Request.Builder();
		builder = builder.url(BASE_URL + path);
		builder = builder.header("Authorization", credential);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				builder = builder.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
		Request request = builder.build();
		try {
			Response response = client.newCall(request).execute();
			return responseHandler.handle(response);
		} catch (IOException e) {
			throw new ExecuteException(e.getMessage(), e);
		} catch (ResponseHandlerException e) {
			throw new ExecuteException(e.getMessage(), e);
		}
	}
	
	public static <T> T doPost(String json, ResponseHandler<T> responseHandler) throws ExecuteException {
		OkHttpClient client = new OkHttpClient();
		RequestBody body = RequestBody.create(JSON, json);
		String credential = Credentials.basic(USERNAME, PASSWORD);
		Request request = new Request.Builder()
				.url(BASE_URL+"/articles/api/articles/")
				.header("Authorization", credential)
				.post(body)
				.build();
		try {
			Response response = client.newCall(request).execute();
			return responseHandler.handle(response);
		} catch (IOException e) {
			throw new ExecuteException(e.getMessage(), e);
		} catch (ResponseHandlerException e) {
			throw new ExecuteException(e.getMessage(), e);
		}
	}

}
