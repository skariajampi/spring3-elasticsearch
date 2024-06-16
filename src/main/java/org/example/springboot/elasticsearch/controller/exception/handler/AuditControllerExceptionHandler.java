package org.example.springboot.elasticsearch.controller.exception.handler;

import org.example.springboot.elasticsearch.service.exception.AuditNotFoundException;
import org.example.springboot.elasticsearch.service.exception.DuplicateCorrelationIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class AuditControllerExceptionHandler {

    @ExceptionHandler(value = {AuditNotFoundException.class, DuplicateCorrelationIdException.class})
    public ResponseEntity<Body> doHandleAuditExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Body(ex.getMessage()));
    }

    public static class Body {

        private String message;

        public Body(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}