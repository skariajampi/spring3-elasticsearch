package org.example.springboot.elasticsearch.service.exception;

public class AuditNotFoundException extends Exception {

    public AuditNotFoundException(String message) {
        super(message);
    }
}
