package com.sparklecow.lootlife.exceptions;

import com.sparklecow.lootlife.controllers.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sparklecow.lootlife.models.exception.BusinessErrorCodes.EMAIL_ALREADY_EXIST;

@Slf4j
@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthenticationControllerAdvice {

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
}
