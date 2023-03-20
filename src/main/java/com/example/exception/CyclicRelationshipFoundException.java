package com.example.exception;

public class CyclicRelationshipFoundException extends RuntimeException {

    public CyclicRelationshipFoundException(String message) {
        super(message);
    }
}
