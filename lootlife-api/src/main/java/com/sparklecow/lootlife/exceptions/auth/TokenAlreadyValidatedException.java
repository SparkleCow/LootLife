package com.sparklecow.lootlife.exceptions.auth;

public class TokenAlreadyValidatedException extends RuntimeException {
    public TokenAlreadyValidatedException(String message) {
        super(message);
    }
}
