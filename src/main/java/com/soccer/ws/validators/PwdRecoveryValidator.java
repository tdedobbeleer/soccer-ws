package com.soccer.ws.validators;

import com.soccer.ws.dto.PasswordRecoveryDTO;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.PwdRecoveryService;
import com.soccer.ws.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 9/12/14.
 */
@Component
public class PwdRecoveryValidator implements Validator {
    private final AccountService accountService;
    private final PwdRecoveryService pwdRecoveryService;

    @Autowired
    public PwdRecoveryValidator(AccountService accountService, PwdRecoveryService pwdRecoveryService) {
        this.accountService = accountService;
        this.pwdRecoveryService = pwdRecoveryService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordRecoveryDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PasswordRecoveryDTO form = (PasswordRecoveryDTO) o;
        ValidationUtils.rejectIfEmpty(errors, "email", "validation.notempty.message");

        if (form.getEmail() != null && accountService.getActiveAccountByEmail(form.getEmail()) == null) {
            errors.rejectValue("email", "validation.email.not.exists");
        } else {
            //Check code for this account
            if (!pwdRecoveryService.isValidRecoveryCode(form.getEmail(), form.getCode())) {
                errors.rejectValue("code", "validation.pwd.recovery.code");
            }
        }

        ValidationUtils.rejectIfEmpty(errors, "code", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "password", "validation.notempty.message");

        if (!ValidationHelper.isPasswordMatch(form.getPassword()))
            errors.rejectValue("password", "validation.complexity.password.message");

    }
}
