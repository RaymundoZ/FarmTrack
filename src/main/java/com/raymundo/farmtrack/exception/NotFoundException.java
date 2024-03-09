package com.raymundo.farmtrack.exception;

import lombok.RequiredArgsConstructor;

public class NotFoundException extends RuntimeException {

    protected final Code code;

    private NotFoundException(Code code, String message, String value) {
        super(message.formatted(value));
        this.code = code;
    }

    @RequiredArgsConstructor
    public enum Code {
        USER_NOT_FOUND("User with '%s' id not found");

        private final String message;

        public NotFoundException get(String value) {
            return new NotFoundException(this, message, value);
        }
    }
}
