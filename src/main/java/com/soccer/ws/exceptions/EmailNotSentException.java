package com.soccer.ws.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by u0090265 on 27.07.17.
 */
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class EmailNotSentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmailNotSentException(String message) {
        super(message);
    }
}
