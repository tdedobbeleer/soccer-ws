package com.soccer.ws.validators;

import com.google.common.base.Strings;
import com.soccer.ws.dto.ProfileDTO;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.utils.GeneralUtils;
import com.soccer.ws.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 11/11/15.
 */
@Component
public class ProfileDTOValidator implements Validator {
    private final AccountService accountService;

    @Autowired
    public ProfileDTOValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProfileDTO dto = (ProfileDTO) o;
        sanitize(dto);

        ValidationUtils.rejectIfEmpty(errors, "account.firstName", "validation.firstName.notEmpty");
        ValidationUtils.rejectIfEmpty(errors, "account.lastName", "validation.lastName.notEmpty");
        ValidationUtils.rejectIfEmpty(errors, "account.username", "validation.userName.notEmpty");


        if (dto.getAddress() != null || !Strings.isNullOrEmpty(dto.getAddress().getCity())) {
            ValidationUtils.rejectIfEmpty(errors, "address.address", "validation.address.notempty.message");
            ValidationUtils.rejectIfEmpty(errors, "address.city", "validation.address.notempty.message");
            ValidationUtils.rejectIfEmpty(errors, "address.postalCode", "validation.address.notempty.message");
        }

        if (!Strings.isNullOrEmpty(dto.getPhone()) && !ValidationHelper.isPhoneMatch(dto.getPhone())) {
            errors.rejectValue("phone", "validation.phone.mismatch");
        }

        if (!Strings.isNullOrEmpty(dto.getMobilePhone()) && !ValidationHelper.isPhoneMatch(dto.getMobilePhone())) {
            errors.rejectValue("mobilePhone", "validation.phone.mismatch");
        }

        if (!errors.hasFieldErrors("account.username") && !ValidationHelper.isEmailMatch(dto.getAccount().getUsername())) {
            errors.rejectValue("account.username", "validation.account.username");
        }

        //Check for username issues
        if (!errors.hasFieldErrors("account.username") && !accountService.isValidUsernameExcludeCurrentId(dto.getAccount().getUsername(), dto.getId())) {
            errors.rejectValue("account.username", "error.duplicate.account.email");
        }
    }

    private void sanitize(ProfileDTO dto) {
        dto.getAddress().setCity(GeneralUtils.trim(dto.getAddress().getCity()));
        dto.getAddress().setAddress(GeneralUtils.trim(dto.getAddress().getAddress()));
        dto.setPhone(GeneralUtils.trim(dto.getPhone()));
        dto.setMobilePhone(GeneralUtils.trim(dto.getMobilePhone()));
    }
}
