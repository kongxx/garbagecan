package my.httpstudy.okhttp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.squareup.okhttp.Response;

public class Test {
	public static void main(String[] args) throws Exception {
//		testGet();
//		testPost();
//		testPut();
//		testDelete();
		
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
		HttpUtils.doPost("/articles/api/articles/", json, new ResponseHandler<Object>() {
			@Override
			public Object handle(Response response) throws ResponseHandlerException {
				if (response.isSuccessful()) {
					try {
						String result = response.body().string();
						System.out.println(result);
						return result;
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
	
	private static void testPut() throws Exception {
		String json = "{\"title\": \"abc\", \"create_user\":1,\"summary\":\"111summarysummarysummarysummary\"}";
		HttpUtils.doPut("/articles/api/articles/20/", json, new ResponseHandler<Object>() {
			@Override
			public Object handle(Response response) throws ResponseHandlerException {
				System.out.println(response.isSuccessful());
				if (response.isSuccessful()) {
					try {
						String result = response.body().string();
						System.out.println(result);
						return result;
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
	
	private static void testDelete() throws Exception {
		HttpUtils.doDelete("/articles/api/articles/34/", new ResponseHandler<Object>() {
			@Override
			public Object handle(Response response) throws ResponseHandlerException {
				System.out.println(response.isSuccessful());
				if (response.isSuccessful()) {
					try {
						String result = response.body().string();
						System.out.println(result);
						return result;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				} else {
					return null;
				}
			}
		});
		
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("article", 1);
//		params.put("user", 2);
//		HttpUtils.doDelete("/articles/api/favorites/0/", params, new ResponseHandler<Object>() {
//			@Override
//			public Object handle(Response response) throws ResponseHandlerException {
//				System.out.println(response.isSuccessful());
//				try {
//					System.out.println(response.code());
//					String result = response.body().string();
//					System.out.println("===" + result);
//					return result;
//				} catch (IOException e) {
//					e.printStackTrace();
//					return null;
//				}
//			}
//		});
	}
}
