package com.mindera.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserCannotChangeException extends RuntimeException {
    public UserCannotChangeException(String message) {
        super(message);
    }
}
