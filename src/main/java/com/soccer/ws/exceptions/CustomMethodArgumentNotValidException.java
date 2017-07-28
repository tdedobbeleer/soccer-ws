package com.soccer.ws.exceptions;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Created by u0090265 on 15/12/16.
 */
public class CustomMethodArgumentNotValidException extends MethodArgumentNotValidException {
    public CustomMethodArgumentNotValidException(BindingResult bindingResult) {
        super(null, bindingResult);
    }

    @Override
    public String getMessage() {
        return "Validation error.";
    }
}
