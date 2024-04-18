package com.company.exps;


import com.company.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerAdvice {


    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<?> on(AlreadyExistException e) {
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(), "Already exist",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

}