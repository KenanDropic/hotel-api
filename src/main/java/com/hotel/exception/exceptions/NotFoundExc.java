package com.hotel.exception.exceptions;

public class NotFoundExc extends RuntimeException {
    public NotFoundExc(String message,Throwable cause){
        super(message,cause);
    }
    public NotFoundExc(String message){
        super(message);
    }

    public NotFoundExc(Throwable cause){
        super(cause);
    }
}
