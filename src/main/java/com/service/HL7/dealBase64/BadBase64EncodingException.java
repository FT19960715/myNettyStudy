package com.service.HL7.dealBase64;

public class BadBase64EncodingException extends Exception {
    private static final long serialVersionUID = 1L;

    public BadBase64EncodingException(String errorMessage) {
        super(errorMessage);
    }
}
