package com.raymundo.farmtrack.util.exception;

import lombok.RequiredArgsConstructor;

public class GradeException extends RuntimeException {

    protected final Code code;

    private GradeException(Code code, String message) {
        super(message);
        this.code = code;
    }

    @RequiredArgsConstructor
    public enum Code {
        WORKING_DAY_NOT_ENDED("The working day is not over yet");

        private final String message;

        public GradeException get() {
            return new GradeException(this, message);
        }
    }
}
