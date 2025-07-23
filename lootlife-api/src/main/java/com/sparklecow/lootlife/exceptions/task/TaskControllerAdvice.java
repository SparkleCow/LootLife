package com.sparklecow.lootlife.exceptions.task;

import com.sparklecow.lootlife.controllers.TasksController;
import com.sparklecow.lootlife.exceptions.ExceptionResponse;
import com.sparklecow.lootlife.exceptions.ForbiddenActionException;
import com.sparklecow.lootlife.exceptions.auth.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.sparklecow.lootlife.models.exception.BusinessErrorCodes.*;
import static com.sparklecow.lootlife.models.exception.BusinessErrorCodes.USER_NOT_FOUND;

@RestControllerAdvice(assignableTypes = TasksController.class)
public class TaskControllerAdvice {

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

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(TaskNotFoundException e){
        return ResponseEntity
                .status(TASK_NOT_FOUND.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TASK_NOT_FOUND.getErrorCode())
                        .businessErrorDescription(TASK_NOT_FOUND.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(ForbiddenActionException e){
        return ResponseEntity
                .status(ILLEGAL_OPERATION.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ILLEGAL_OPERATION.getErrorCode())
                        .businessErrorDescription(ILLEGAL_OPERATION.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(TaskExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(TaskExpiredException e){
        return ResponseEntity
                .status(TASK_EXPIRED.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TASK_EXPIRED.getErrorCode())
                        .businessErrorDescription(TASK_EXPIRED.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(TaskAlreadyCompletedException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(TaskAlreadyCompletedException e){
        return ResponseEntity
                .status(TASK_ALREADY_COMPLETED.getHttpStatus())
                .body(ExceptionResponse.builder()
                        .businessErrorCode(TASK_ALREADY_COMPLETED.getErrorCode())
                        .businessErrorDescription(TASK_ALREADY_COMPLETED.getMessage() + " " +e.getMessage())
                        .message(e.getMessage())
                        .build());
    }
}
