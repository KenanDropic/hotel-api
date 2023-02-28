package com.hotel.exception.exceptions;

public class ForbiddenExc extends RuntimeException {
    public ForbiddenExc(String message, Throwable cause){
        super(message,cause);
    }
    public ForbiddenExc(String message){
        super(message);
    }
    public ForbiddenExc(Throwable cause){
        super(cause);
    }

}

