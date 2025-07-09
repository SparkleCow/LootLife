package com.sparklecow.lootlife.exceptions;

import com.sparklecow.lootlife.controllers.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthenticationControllerAdvice {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException e){
        return ResponseEntity
                .status(MESSAGE_ERROR.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(MESSAGE_ERROR.getErrorCode())
                        .businessErrorDescription(MESSAGE_ERROR.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }
}
