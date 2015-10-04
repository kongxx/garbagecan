package my.httpstudy.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.fluent.Executor;

public class Test {
	public static void main(String[] args) throws Exception {
		Credentials credentials = new UsernamePasswordCredentials("admin", "Letmein");
//		Executor executor = Executor.newInstance().auth(new HttpHost("127.0.0.1", 8000), credentials);
//		Executor executor = Executor.newInstance().auth(new HttpHost("127.0.0.1", 8000, "http"), credentials);
//		executor.auth(AuthScope.ANY, credentials).execute(Request.Get("http://127.0.0.1:8000/articles/api/articles/")).returnContent().asString();
		Executor executor = Executor.newInstance()
				.auth(new HttpHost("127.0.0.1", 8000), "admin", "Letmein")
				.auth(new HttpHost("localhost", 8000), "admin", "Letmein")
				.auth(new HttpHost("mydesktop", 8000), "admin", "Letmein");
//		executor.execute(Request.Get("http://127.0.0.1:8000/articles/api/articles/")).returnContent().asString();

//		Request.Get("http://127.0.0.1:8000/articles/api/articles/").execute().returnContent();
//		Content content = Request.Get("http://localhost:8000/articles/api/articles/")
//				.execute().returnContent();
//		System.out.println(content);
//		Request.Post("http://targethost/login")
//				.bodyForm(Form.form().add("username",  "vip").add("password",  "secret").build())
//				.execute().returnContent();

//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpHost target = new HttpHost("localhost", 8000, "http");
//		HttpGet httpget = new HttpGet("/articles/api/articles/");
//		CloseableHttpResponse response = httpclient.execute(httpget);
//		System.out.println(response.getEntity().getContent());


	}
}
