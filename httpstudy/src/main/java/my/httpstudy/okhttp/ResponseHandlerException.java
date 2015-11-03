package my.httpstudy.okhttp;

public class ResponseHandlerException extends Exception {
	public ResponseHandlerException() {
		super();
	}

	public ResponseHandlerException(String message) {
		super(message);
	}

	public ResponseHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResponseHandlerException(Throwable cause) {
		super(cause);
	}
}
