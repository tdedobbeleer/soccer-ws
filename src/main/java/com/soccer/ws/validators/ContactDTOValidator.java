package com.soccer.ws.validators;

import com.soccer.ws.dto.ContactDTO;
import com.soccer.ws.utils.ValidationHelper;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 12/31/14.
 */
@Component
public class ContactDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ContactDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ContactDTO dto = (ContactDTO) o;
        ValidationUtils.rejectIfEmpty(errors, "message", "validation.notempty.message");

        if (!ValidationHelper.isEmailMatch(dto.getEmail())) {
            errors.rejectValue("email", "validation.noEmail");
        }
        dto.setMessage(SanitizeUtils.sanitizeHtml(dto.getMessage()));
    }
}
