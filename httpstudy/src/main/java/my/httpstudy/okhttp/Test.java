package my.httpstudy.okhttp;

import java.io.IOException;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;

public class Test {
	public static void main(String[] args) throws Exception {
		testGet();
	}
	
	private static void testGet() throws Exception {
		Object obj = HttpUtils.doGet("/articles/api/articles/", new ResponseHandler<Object>() {
			@Override
			public Object handle(Response response) throws RuntimeException {
				if (response.isSuccessful()) {
					try {
						return response.body().string();
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					return null;
				}
			}
		});
		System.out.println(obj);
	}
	
	private static void testPost() throws Exception {
		String json = "{\"title\": \"abc\", \"create_user\":1,\"summary\":\"summarysummarysummarysummary\"}";
		HttpUtils.doPost(json, new ResponseHandler<Object>() {
			@Override
			public Object handle(Response response) throws ResponseHandlerException {
				if (response.isSuccessful()) {
					try {
						System.out.println(response.body().string());
						return response.body().string();
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					return null;
				}
			}
		});
	}
}
