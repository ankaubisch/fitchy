package de.kaubisch.fitchy.exception;

public class InvokeException extends Exception {

	public InvokeException() {
		super();
	}

	public InvokeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvokeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvokeException(String message) {
		super(message);
	}

	public InvokeException(Throwable cause) {
		super(cause);
	}
}
