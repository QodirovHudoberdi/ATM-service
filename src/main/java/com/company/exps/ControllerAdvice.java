package com.company.exps;


import com.company.dto.response.ResponseMessage;
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
 public ResponseEntity<?> on(NotFoundException e) {
        final ResponseMessage result = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Not Found",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
public ResponseEntity<?> on(NotAllowedExceptions e) {
        final ResponseMessage result = new ResponseMessage(HttpStatus.METHOD_NOT_ALLOWED.value(), "Not Allowed for you",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }

}