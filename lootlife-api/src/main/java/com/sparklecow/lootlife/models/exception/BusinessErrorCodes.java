package com.sparklecow.lootlife.models.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code "),

    VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation error "),
    TOKEN_ALREADY_VALIDATE(400, HttpStatus.BAD_REQUEST, "Token has been validate before "),
    EMAIL_ALREADY_EXIST(400, HttpStatus.BAD_REQUEST, "Email already has been used "),

    BAD_CREDENTIALS(401, HttpStatus.UNAUTHORIZED, "Email / password is incorrect "),
    TOKEN_INVALID(401, HttpStatus.UNAUTHORIZED, "Invalid token "),

    ACCOUNT_DISABLED(403, HttpStatus.FORBIDDEN, "User account is disabled "),
    ILLEGAL_OPERATION(403, HttpStatus.FORBIDDEN, "Illegal operation "),
    TASK_ALREADY_COMPLETED(403, HttpStatus.FORBIDDEN, "Task has already been completed "),

    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Email / password is incorrect "),
    TOKEN_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Token not found "),
    ROLE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Role not found "),
    TASK_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Task not found "),
    STATS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Stats not found "),

    METHOD_NOT_ALLOWED(405, HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed "),

    TOKEN_EXPIRED(410, HttpStatus.GONE, "Token expired "),
    TASK_EXPIRED(410, HttpStatus.GONE, "Task expired "),

    ACCOUNT_LOCKED(423, HttpStatus.LOCKED, "User account is locked "),

    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error "),
    MESSAGE_ERROR(500,  HttpStatus.INTERNAL_SERVER_ERROR, "Error sending message ");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    BusinessErrorCodes(int errorCode, HttpStatus httpStatus, String message) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
