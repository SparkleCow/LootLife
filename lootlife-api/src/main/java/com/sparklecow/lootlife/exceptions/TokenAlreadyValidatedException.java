package com.sparklecow.lootlife.exceptions;

public class TokenAlreadyValidatedException extends RuntimeException {
    public TokenAlreadyValidatedException(String message) {
        super(message);
    }
}
