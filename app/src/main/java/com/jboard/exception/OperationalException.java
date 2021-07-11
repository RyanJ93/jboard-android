package com.jboard.exception;

public class OperationalException extends JBoardException {
    public OperationalException() {
        super();
    }

    public OperationalException(String message) {
        super(message);
    }

    public OperationalException(String message, Throwable cause) {
        super(message, cause);
    }
}
