package com.projectx.resume_service.exceptions;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String msg){
        super(msg);
    }
}
