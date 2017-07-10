package com.soccer.ws.validators;

import com.soccer.ws.dto.AccountDTO;
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
public class AccountValidator implements Validator {
    private final AccountService accountService;

    @Autowired
    public AccountValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDTO.class.equals(clazz);
    }

    public void validate(Object o, Errors errors) {
        AccountDTO accountDTO = (AccountDTO) o;
        sanitize(accountDTO);

        ValidationUtils.rejectIfEmpty(errors, "username", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "firstName", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "lastName", "validation.notempty.message");

        if (!ValidationHelper.isNameMatch(accountDTO.getFirstName())) {
            errors.rejectValue("firstName", "validation.name.mismatch");
        }

        if (!ValidationHelper.isLength(accountDTO.getFirstName(), Constants.TWO)) {
            errors.rejectValue("firstName", "validation.minimum.length", new Object[]{Constants.TWO}, "validation.minimum.length");
        }

        if (!ValidationHelper.isNameMatch(accountDTO.getLastName())) {
            errors.rejectValue("lastName", "validation.name.mismatch");
        }

        if (!ValidationHelper.isLength(accountDTO.getLastName(), Constants.TWO)) {
            errors.rejectValue("lastName", "validation.minimum.length", new Object[]{Constants.TWO}, "validation.minimum.length");
        }
    }

    private void sanitize(AccountDTO accountDTO) {
        accountDTO.setFirstName(GeneralUtils.trim(accountDTO.getFirstName()));
        accountDTO.setLastName(GeneralUtils.trim(accountDTO.getLastName()));
    }
}
