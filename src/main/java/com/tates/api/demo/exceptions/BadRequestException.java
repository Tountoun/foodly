package com.tates.api.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    public BadRequestException() {
        super("Mauvaise requête, vous devez penser autrement");
    }
    public BadRequestException(String message) {
        super(message);
    }
}
