package com.tus.order.exception;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, String value) {
        super(String.format("%s not found with %s: '%s'", resource, field, value));
    }

}
