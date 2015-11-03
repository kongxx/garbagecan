package my.httpstudy.okhttp;

import java.io.IOException;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.Response;

public class Test {
	public static void main(String[] args) throws Exception {
//		Object obj = HttpUtils.doGet("/articles/api/articles/", new ResponseHandler<Object>() {
//			@Override
//			public Object handle(Response response) throws RuntimeException {
//				if (response.isSuccessful()) {
//					try {
//						return response.body().string();
//					} catch (IOException e) {
//						e.printStackTrace();
//						return null;
//					}
//				} else {
//					return null;
//				}
//			}
//		});
//		System.out.println(obj);
		
//		HttpUtils.doPost(new ResponseHandler<Object>() {
//			@Override
//			public Object handle(Response response) throws ResponseHandlerException {
//				if (response.isSuccessful()) {
//					try {
//						return response.body().string();
//					} catch (IOException e) {
//						e.printStackTrace();
//						return null;
//					}
//				} else {
//					return null;
//				}
//			}
//			
//		});
		
		OkHttpClient client = new OkHttpClient();
		String credential = Credentials.basic("admin", "Letmein");
		Builder builder = new Request.Builder();
		builder = builder.url("http://192.168.145.100:8000/articles/api/comments/");
		builder = builder.addHeader("article_id", "10");
		builder = builder.header("Authorization", credential);
		Request request = builder.build();
		
		Response response = client.newCall(request).execute();
		System.out.println(response.isSuccessful());
		System.out.println(response.body().string());
	}
}
