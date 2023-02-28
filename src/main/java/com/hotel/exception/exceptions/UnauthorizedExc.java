package com.hotel.exception.exceptions;

public class UnauthorizedExc extends RuntimeException {
    public UnauthorizedExc(String message, Throwable cause){
        super(message,cause);
    }
    public UnauthorizedExc(String message){
        super(message);
    }

    public UnauthorizedExc(Throwable cause){
        super(cause);
    }
}


