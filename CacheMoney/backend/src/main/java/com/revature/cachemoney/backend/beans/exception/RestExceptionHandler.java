package com.revature.cachemoney.backend.beans.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

/**
 * Controller to handle requests related to Users.
 *
 * @author Phillip Vo, Josue Rodriguez, Prakash, Maikel Vera
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { EntityNotFoundException.class })
    protected ResponseEntity<Object> persistentEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { MissingRequestHeaderException.class})
    protected ResponseEntity<Object> missingElementsException(MissingRequestHeaderException ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { JsonProcessingException.class })
    protected ResponseEntity<Object> jsonProcessingException(JsonProcessingException ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = { QrGenerationException.class })
    protected ResponseEntity<Object> qrGenerationException(QrGenerationException ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> generalHandleException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}