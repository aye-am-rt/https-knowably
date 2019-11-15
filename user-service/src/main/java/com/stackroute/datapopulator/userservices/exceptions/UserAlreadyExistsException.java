package com.stackroute.datapopulator.userservices.exceptions;

public class UserAlreadyExistsException extends UserException{
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
