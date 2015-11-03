package my.httpstudy.okhttp;

public class ExecuteException extends Exception {
	public ExecuteException() {
		super();
	}

	public ExecuteException(String message) {
		super(message);
	}

	public ExecuteException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecuteException(Throwable cause) {
		super(cause);
	}
}
