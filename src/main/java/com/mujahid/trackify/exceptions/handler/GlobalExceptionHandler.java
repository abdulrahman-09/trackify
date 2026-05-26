package com.mujahid.trackify.exceptions.handler;

import com.mujahid.trackify.domain.dto.response.ErrorResponse;
import com.mujahid.trackify.exceptions.EmailAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex){
        return build(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), null);
    }

//    @ExceptionHandler(Exception.class)
//    public void handleAnyException(Exception ex){
//        System.out.println(ex.toString());
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return build(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "One or more fields are invalid",
                errors
        );
    }


    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message, Map<String, String> validationErrors) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(status.value(), error, message, LocalDateTime.now(), validationErrors)
        );
    }
}
