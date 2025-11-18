package com.noprobit.servingwebcontent.exception;

public class PersonAlreadyExistException extends RuntimeException {

    public PersonAlreadyExistException(String message) {
        super(message);
    }

    public PersonAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
