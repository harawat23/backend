package com.example.appspringdata.Exceptions;

public class OperationFailedException extends RuntimeException{
    public OperationFailedException(String msg){
        super(msg);
    }
}