package com.soccer.ws.validators;

import com.soccer.ws.dto.PasswordDTO;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 13.08.17.
 */
@Component
public class PasswordDTOValidator implements Validator {
    private final AccountService accountService;

    @Autowired
    public PasswordDTOValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordDTO dto = (PasswordDTO) target;

        ValidationUtils.rejectIfEmpty(errors, "id", "validation.notEmpty");
        ValidationUtils.rejectIfEmpty(errors, "newPassword", "validation.newPassword.notEmpty");
        ValidationUtils.rejectIfEmpty(errors, "oldPassword", "validation.oldPassword.notEmpty");

        if (!ValidationHelper.isPasswordMatch(dto.getNewPassword())) {
            errors.rejectValue("newPassword", "validation.complexity.newpassword.message");
        }

        if (!accountService.checkOldPassword(dto.getId(), dto.getOldPassword())) {
            ValidationUtils.rejectIfEmpty(errors, "oldPassword", "validation.oldPassword.noMatch");
        }
    }
}
