package com.nasa.exception;

public class PicOfDayServiceException extends RuntimeException {
    public PicOfDayServiceException(String message) {
        super(message);
    }

    public PicOfDayServiceException(Exception e) {
        super(e);
    }
}
