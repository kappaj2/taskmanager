package com.ajk.taskman.web.controller;

import com.ajk.taskman.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.PersistenceException;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
public class TaskManControllerAdvise extends ResponseEntityExceptionHandler {

    /**
     * Handle al the SQL Constraint Violations.
     * Error code will be 400 - Bad Request.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Object> handleSQLException(final SQLIntegrityConstraintViolationException ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.valueOf(400), ex.getLocalizedMessage(), bodyOfResponse);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    /**
     * Handle all the duplicate errors that might be thrown.
     * Error code will be 409 - Conflict.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({UserAlreadyExistsException.class, TaskAlreadyExistsException.class})
    public ResponseEntity<Object> handleRecordAlreadyExistsException(final Exception ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.valueOf(409), ex.getLocalizedMessage(), bodyOfResponse);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle all the NotFound exceptions.
     * Error code will be 400 - Bad Request.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({UserNotFoundException.class, TaskNotFoundException.class, TaskStatusNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.valueOf(400), ex.getLocalizedMessage(), bodyOfResponse);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    /**
     * Handle all the rest of the exceptions. These are errors not checked for in the application and not handled above.
     * Error code will be 400 - Bad Request.
     */
    @ExceptionHandler(value = {PersistenceException.class})
    protected ResponseEntity<Object> handleGenericPersistenceExcpetions(Exception ex, WebRequest request) {

        String bodyOfResponse = ex.getLocalizedMessage();
        String message = ex.getMessage();
        log.error("Persistence exception occurred : " + bodyOfResponse);
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, bodyOfResponse);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handlException(final Exception ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.valueOf(500), ex.getLocalizedMessage(), bodyOfResponse);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
