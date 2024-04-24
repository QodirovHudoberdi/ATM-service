package com.company.exps.advice;


import com.company.dto.response.ResponseMessage;
import com.company.entity.Card;
import com.company.exps.*;
import com.company.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ControllerAdvice {
    private final static Logger LOG = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<?> on(AlreadyExistException e) {
        LOG.error(e.getMessage());
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> on(NotFoundException e) {
        LOG.error(e.getMessage());
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @ExceptionHandler(NotAllowedExceptions.class)
    public ResponseEntity<?> on(NotAllowedExceptions e) {
        LOG.error(e.getMessage());
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }

    @ExceptionHandler(BankNoteException.class)
    public ResponseEntity<?> on(BankNoteException e) {
        LOG.error(e.getMessage());
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @ExceptionHandler(BankNoteTypeException.class)
    public ResponseEntity<?> on(BankNoteTypeException e) {
        LOG.error(e.getMessage());
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @ExceptionHandler(CardException.class)
    public ResponseEntity<?> on(CardException e) {
        LOG.error(e.getMessage());
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @ExceptionHandler(CardHolderException.class)
    public ResponseEntity<?> on(CardHolderException e) {
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @ExceptionHandler(CardTypeException.class)
    public ResponseEntity<?> on(CardTypeException e) {
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @ExceptionHandler(CurrencyException.class)
    public ResponseEntity<?> on(CurrencyException e) {
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @ExceptionHandler(HistoryException.class)
    public ResponseEntity<?> on(HistoryException e) {
        final ResponseMessage result = new ResponseMessage(HttpStatus.CONFLICT.value(),
                e.getMessage());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> on(MethodArgumentNotValidException ex) {
        ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponse errorResponse = ResponseMessage.of(ResponseCode.REQUIRED_DATA_MISSING, ex.getBindingResult()
                .getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

    }
}