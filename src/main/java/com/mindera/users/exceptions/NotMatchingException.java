package com.mindera.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)

public class NotMatchingException extends RuntimeException {
    public NotMatchingException(String message) {
        super(message);
    }
}
