package com.raymundo.farmtrack.util.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {

    protected final Code code;

    private AuthException(Code code, String message) {
        super(message);
        this.code = code;
    }

    @RequiredArgsConstructor
    public enum Code {
        TOKENS_EXPIRED("Tokens expired. You need to authorize"),
        BAD_CREDENTIALS("Invalid credentials"),
        NOT_ENOUGH_RIGHTS("Not enough rights for this action"),
        ACCOUNT_BLOCKED("Your account has been blocked");

        private final String message;

        public AuthException get() {
            return new AuthException(this, message);
        }
    }
}
