package com.jboard.exception;

public class UserNotFoundException extends JBoardException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorMessage(){
        return "No such user found.";
    }
}
