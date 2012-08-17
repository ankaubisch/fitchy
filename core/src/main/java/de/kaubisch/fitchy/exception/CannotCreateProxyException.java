package de.kaubisch.fitchy.exception;

/**
 * 
 * @author Andreas Kaubisch <andreas.kaubisch@gmail.com>
 * Date: 8/15/12
 * Time: 9:10 PM
 */
public class CannotCreateProxyException extends RuntimeException {

	public CannotCreateProxyException() {
		super();
	}

	public CannotCreateProxyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CannotCreateProxyException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotCreateProxyException(String message) {
		super(message);
	}

	public CannotCreateProxyException(Throwable cause) {
		super(cause);
	}

}
