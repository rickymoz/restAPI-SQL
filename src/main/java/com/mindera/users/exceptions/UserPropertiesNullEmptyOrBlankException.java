package com.mindera.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserPropertiesNullEmptyOrBlankException extends RuntimeException {
    public UserPropertiesNullEmptyOrBlankException(String message) {
        super(message);
    }
}
