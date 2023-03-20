package com.example.exception;

public class NoRootFoundException extends RuntimeException {

    public NoRootFoundException(String message) {
        super(message);
    }
}
