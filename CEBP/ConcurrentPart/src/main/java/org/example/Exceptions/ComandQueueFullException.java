package org.example.Exceptions;


public class ComandQueueFullException extends RuntimeException {
    public ComandQueueFullException(String message) {
        super(message);
    }
}