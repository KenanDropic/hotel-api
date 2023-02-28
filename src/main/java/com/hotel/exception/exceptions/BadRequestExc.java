package com.hotel.exception.exceptions;

public class BadRequestExc extends RuntimeException{
    public BadRequestExc(String message,Throwable cause){
        super(message,cause);
    }
    public BadRequestExc(String message){
        super(message);
    }

    public BadRequestExc(Throwable cause){
        super(cause);
    }
}
