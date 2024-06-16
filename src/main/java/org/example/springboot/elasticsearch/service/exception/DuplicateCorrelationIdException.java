package org.example.springboot.elasticsearch.service.exception;

public class DuplicateCorrelationIdException extends Throwable {
    public DuplicateCorrelationIdException(String message) {
        super(message);
    }
}
