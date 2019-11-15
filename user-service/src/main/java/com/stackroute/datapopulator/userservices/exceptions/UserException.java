package com.stackroute.datapopulator.userservices.exceptions;

public abstract class UserException extends Exception{
    public UserException (String msg){
        super(msg);
    }
}
