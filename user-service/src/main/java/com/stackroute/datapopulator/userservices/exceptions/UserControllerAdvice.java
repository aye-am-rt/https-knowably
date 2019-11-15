package com.stackroute.datapopulator.userservices.exceptions;

import com.stackroute.datapopulator.userservices.error.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsExceptionHandler(UserAlreadyExistsException e){
        String httpError = e.getClass().toString()+": "+e.getMessage();
        return new ResponseEntity<CustomError>(new CustomError(httpError, HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundExceptionHandler(UserNotFoundException e){
        String httpError = e.getClass().toString()+": "+e.getMessage();
        return new ResponseEntity<CustomError>(new CustomError(httpError, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> internalServerErrorExceptionHandler(Exception e){
        String httpError = e.getClass().toString()+": "+e.getMessage();
        return new ResponseEntity<CustomError>(new CustomError(httpError, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
