package com.soccer.ws.validators;

import com.google.common.base.Strings;
import com.soccer.ws.dto.ProfileDTO;
import com.soccer.ws.utils.GeneralUtils;
import com.soccer.ws.utils.ValidationHelper;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 11/11/15.
 */
@Component
public class ProfileValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProfileDTO dto = (ProfileDTO) o;
        sanitize(dto);

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
    }

    private void sanitize(ProfileDTO dto) {
        dto.getAddress().setCity(GeneralUtils.trim(dto.getAddress().getCity()));
        dto.getAddress().setAddress(GeneralUtils.trim(dto.getAddress().getAddress()));
        dto.setPhone(GeneralUtils.trim(dto.getPhone()));
        dto.setMobilePhone(GeneralUtils.trim(dto.getMobilePhone()));
    }
}
