package my.httpstudy.jersey;

import javax.ws.rs.core.Response;

public interface ResponseHandler<T> {
	T handle(Response response) throws RuntimeException;
}
