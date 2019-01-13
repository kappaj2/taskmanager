package com.ajk.taskman.exception;

public class TaskStatusNotFoundException extends RuntimeException {

    public TaskStatusNotFoundException() {
        super();
    }

    public TaskStatusNotFoundException(String message) {
        super(message);
    }

    public TaskStatusNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskStatusNotFoundException(Throwable cause) {
        super(cause);
    }

    protected TaskStatusNotFoundException(String message,
                                    Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
