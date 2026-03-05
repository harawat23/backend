package com.example.appspringdata.Exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleResourceNotFound(ResourceNotFoundException ex){
        return buildErrorResponse(HttpStatus.NOT_FOUND,"Not Found",ex.getMessage());
    }

    @ExceptionHandler(BadRequestsException.class)
    public ResponseEntity<Map<String,Object>> handleBadRequest(BadRequestsException ex){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String,Object>> handleConflict(ConflictException ex){
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Conflict", ex.getMessage());
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<Map<String,Object>> handleOperationFailed(OperationFailedException ex){
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGenericException(Exception ex){
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Somethin Unexpected Happened");
    }

    private ResponseEntity<Map<String,Object>> buildErrorResponse(HttpStatus status,String error,String message){
        Map<String,Object> response=new HashMap<>();
        response.put("timestamp",LocalDateTime.now());
        response.put("status",status.value());
        response.put("error",error);
        response.put("message",message);
        return ResponseEntity.status(status).body(response);
    }
}
