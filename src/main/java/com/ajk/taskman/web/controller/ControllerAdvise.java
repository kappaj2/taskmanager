package com.ajk.taskman.web.controller;

import com.ajk.taskman.exception.TaskNotFoundException;
import com.ajk.taskman.exception.UserNotFoundException;
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
public class ControllerAdvise extends ResponseEntityExceptionHandler {

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Object> handleSQLException(final SQLIntegrityConstraintViolationException ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.valueOf(404), ex.getLocalizedMessage(), bodyOfResponse);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle all the NotFound exceptions.
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({UserNotFoundException.class, TaskNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.valueOf(400), ex.getLocalizedMessage(), bodyOfResponse);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    /**
     * Handle all the possible persistence exceptions.
     */
    @ExceptionHandler(value = {PersistenceException.class})
    protected ResponseEntity<Object> handlePersistenceExcpetions(Exception ex, WebRequest request) {

        String bodyOfResponse = ex.getLocalizedMessage();
        String message = ex.getMessage();

        log.error("Persistence exception occurred : " + bodyOfResponse);

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, bodyOfResponse);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
