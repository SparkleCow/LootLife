package com.sparklecow.lootlife.models.exception;

import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),

    VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation error"),
    TOKEN_ALREADY_VALIDATE(400, HttpStatus.BAD_REQUEST, "Token has been validate before"),

    BAD_CREDENTIALS(401, HttpStatus.UNAUTHORIZED, "Email / password is incorrect"),
    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "Token expired"),
    TOKEN_INVALID(401, HttpStatus.UNAUTHORIZED, "Invalid token"),

    ACCOUNT_DISABLED(403, HttpStatus.FORBIDDEN, "User account is disabled"),
    ILLEGAL_OPERATION(403, HttpStatus.FORBIDDEN, "Illegal operation"),

    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Email / password is incorrect"),
    TOKEN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Token not found"),
    ROLE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Role not found"),
    TASK_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Task not found"),
    STATS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Stats not found"),

    METHOD_NOT_ALLOWED(405, HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),

    ACCOUNT_LOCKED(423, HttpStatus.LOCKED, "User account is locked"),

    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    MESSAGE_ERROR(500,  HttpStatus.INTERNAL_SERVER_ERROR, "Error sending message");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    BusinessErrorCodes(int errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
