package com.example.appspringdata.Exceptions;

public class BadRequestsException extends RuntimeException{
    public BadRequestsException(String msg){
        super(msg);
    }
}