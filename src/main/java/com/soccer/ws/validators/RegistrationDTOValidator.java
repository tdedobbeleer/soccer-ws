package com.soccer.ws.validators;

import com.soccer.ws.dto.RegistrationDTO;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.utils.Constants;
import com.soccer.ws.utils.GeneralUtils;
import com.soccer.ws.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 12/9/15.
 */
@Component
public class RegistrationDTOValidator implements Validator {
    private final AccountService accountService;

    @Autowired
    public RegistrationDTOValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationDTO.class.equals(clazz);
    }

    public void validate(Object o, Errors errors) {
        RegistrationDTO registrationDTO = (RegistrationDTO) o;
        sanitize(registrationDTO);

        ValidationUtils.rejectIfEmpty(errors, "email", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "firstName", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "lastName", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "password", "validation.notempty.message");

        if (!ValidationHelper.isNameMatch(registrationDTO.getFirstName())) {
            errors.rejectValue("firstName", "validation.name.mismatch");
        }

        if (!ValidationHelper.isLength(registrationDTO.getFirstName(), Constants.TWO)) {
            errors.rejectValue("firstName", "validation.minimum.length", new Object[]{Constants.TWO}, "validation.minimum.length");
        }

        if (!ValidationHelper.isNameMatch(registrationDTO.getLastName())) {
            errors.rejectValue("lastName", "validation.name.mismatch");
        }

        if (!ValidationHelper.isLength(registrationDTO.getLastName(), Constants.TWO)) {
            errors.rejectValue("lastName", "validation.minimum.length", new Object[]{Constants.TWO}, "validation.minimum.length");
        }

        if (!ValidationHelper.isEmailMatch(registrationDTO.getEmail())) {
            errors.rejectValue("email", "validation.noEmail", "validation.noEmail");

        } else {
            if (!accountService.isValidUsername(registrationDTO.getEmail())) {
                errors.rejectValue("email", "error.duplicate.account.email",
                        new String[]{registrationDTO.getEmail()}, null);
            }
        }
    }

    private void sanitize(RegistrationDTO registrationDTO) {
        registrationDTO.setFirstName(GeneralUtils.trim(registrationDTO.getFirstName()));
        registrationDTO.setLastName(GeneralUtils.trim(registrationDTO.getLastName()));
        registrationDTO.setLastName(GeneralUtils.trim(registrationDTO.getLastName()));
    }
}
