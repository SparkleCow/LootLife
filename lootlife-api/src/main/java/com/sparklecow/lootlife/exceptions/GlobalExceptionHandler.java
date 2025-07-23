package com.sparklecow.lootlife.exceptions;

import com.sparklecow.lootlife.exceptions.auth.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sparklecow.lootlife.models.exception.BusinessErrorCodes.USER_NOT_FOUND;
import static com.sparklecow.lootlife.models.exception.BusinessErrorCodes.VALIDATION_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessages.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        });

        return ResponseEntity
                .status(VALIDATION_ERROR.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(VALIDATION_ERROR.getErrorCode())
                        .businessErrorDescription(VALIDATION_ERROR.getMessage() + errorMessages)
                        .message(ex.getMessage())
                        .build());
    }

    //For filters and interceptors.
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
}
