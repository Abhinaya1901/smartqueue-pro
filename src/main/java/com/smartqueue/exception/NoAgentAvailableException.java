package com.smartqueue.exception;

public class NoAgentAvailableException extends RuntimeException {
    public NoAgentAvailableException() {
        super("No available agent found for this ticket");
    }
}