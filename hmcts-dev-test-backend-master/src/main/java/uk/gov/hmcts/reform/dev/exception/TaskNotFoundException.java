package uk.gov.hmcts.reform.dev.exception;

public class TaskNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public TaskNotFoundException() {
        super();
    }

    public TaskNotFoundException(String message) {
        super(message);
    }
}
