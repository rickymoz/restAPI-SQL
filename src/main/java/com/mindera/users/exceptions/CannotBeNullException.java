package com.mindera.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CannotBeNullException extends RuntimeException {
    public CannotBeNullException(String message) {
        super(message);
    }
}
