package com.ajk.taskman.exception;

public class TaskAlreadyExistsException extends RuntimeException {

    public TaskAlreadyExistsException() {
        super();
    }

    public TaskAlreadyExistsException(String message) {
        super(message);
    }

    public TaskAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected TaskAlreadyExistsException(String message,
                                         Throwable cause,
                                         boolean enableSuppression,
                                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
