package my.httpstudy.okhttp;

import com.squareup.okhttp.Response;

public interface ResponseHandler<T> {
	T handle(Response response) throws ResponseHandlerException;
}
