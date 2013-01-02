package jurbanairship;

public class PushException extends Exception {

	private static final long serialVersionUID = 1L;

	public PushException() {
		super();
	}

	public PushException(String message) {
		super(message);
	}

	public PushException(String message, Throwable cause) {
		super(message, cause);
	}

	public PushException(Throwable cause) {
		super(cause);
	}
}
