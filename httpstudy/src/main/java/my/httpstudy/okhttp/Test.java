package my.httpstudy.okhttp;

import java.io.IOException;

import com.squareup.okhttp.Response;

public class Test {
	public static void main(String[] args) throws Exception {
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
}
