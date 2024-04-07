package com.nocta.login.service.handler;

import com.nocta.internalization.service.MessageService;
import com.nocta.login.service.payload.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageService messageService;

    @ExceptionHandler(value
            = { Exception.class})
    protected ResponseEntity<Object> handleException(
            RuntimeException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(400))
                .body(new ErrorResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(value
            = { AuthenticationException.class})
    protected ResponseEntity<Object> handleAuthenticationException(
            RuntimeException ex, WebRequest request) {
            return ResponseEntity
                .status(HttpStatusCode.valueOf(401))
                .body(new ErrorResponse(false, messageService.getMessage("", "fail.auth")));
    }


}