package com.soccer.ws.validators;

import com.soccer.ws.dto.MatchDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 5/17/14.
 */
@Component
public class CreateMatchValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return MatchDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MatchDTO dto = (MatchDTO) o;
        ValidationUtils.rejectIfEmpty(errors, "date", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "homeTeam", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "awayTeam", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "season", "validation.notempty.message");
    }
}
