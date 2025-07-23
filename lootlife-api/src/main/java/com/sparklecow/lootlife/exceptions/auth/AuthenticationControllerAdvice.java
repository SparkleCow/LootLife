package com.sparklecow.lootlife.exceptions.auth;

import com.sparklecow.lootlife.controllers.AuthController;
import com.sparklecow.lootlife.exceptions.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sparklecow.lootlife.models.exception.BusinessErrorCodes.*;

@Slf4j
@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthenticationControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e){
        return ResponseEntity
                .status(BAD_CREDENTIALS.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getErrorCode())
                        .businessErrorDescription(BAD_CREDENTIALS.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException e){
        return ResponseEntity
                .status(USER_NOT_FOUND.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(USER_NOT_FOUND.getErrorCode())
                        .businessErrorDescription(USER_NOT_FOUND.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingException(EmailAlreadyExistsException e){
        return ResponseEntity
                .status(EMAIL_ALREADY_EXIST.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(EMAIL_ALREADY_EXIST.getErrorCode())
                        .businessErrorDescription(EMAIL_ALREADY_EXIST.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRoleNotFoundException(RoleNotFoundException e){
        return ResponseEntity
                .status(ROLE_NOT_FOUND.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ROLE_NOT_FOUND.getErrorCode())
                        .businessErrorDescription(ROLE_NOT_FOUND.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(TokenAlreadyValidatedException.class)
    public ResponseEntity<ExceptionResponse> handleTokenAlreadyValidatedException(TokenAlreadyValidatedException e){
        return ResponseEntity
                    .status(TOKEN_ALREADY_VALIDATE.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TOKEN_ALREADY_VALIDATE.getErrorCode())
                        .businessErrorDescription(TOKEN_ALREADY_VALIDATE.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleTokenExpiredException(TokenExpiredException e){
        return ResponseEntity
                .status(TOKEN_EXPIRED.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TOKEN_EXPIRED.getErrorCode())
                        .businessErrorDescription(TOKEN_EXPIRED.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTokenNotFoundException(TokenNotFoundException e){
        return ResponseEntity
                .status(TOKEN_NOT_FOUND.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TOKEN_NOT_FOUND.getErrorCode())
                        .businessErrorDescription(TOKEN_NOT_FOUND.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }
}
