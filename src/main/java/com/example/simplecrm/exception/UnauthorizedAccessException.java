package com.example.simplecrm.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("Order does not belong to this customer");
    }
}
