package org.gamenet.application.VerseLearner.data;

public class ReferenceException extends Exception {
	private static final long serialVersionUID = 1L;

	public ReferenceException() {
		super();
	}

	public ReferenceException(String message) {
		super(message);
	}

	public ReferenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReferenceException(Throwable cause) {
		super(cause);
	}

}
