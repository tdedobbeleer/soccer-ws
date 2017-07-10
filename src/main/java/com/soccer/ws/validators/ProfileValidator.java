package com.soccer.ws.validators;

import com.google.common.base.Strings;
import com.soccer.ws.dto.ProfileDTO;
import com.soccer.ws.utils.GeneralUtils;
import com.soccer.ws.utils.ValidationHelper;
import org.apache.commons.lang3.StringUtils;
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

        if (!Strings.isNullOrEmpty(dto.getAddress()) || !Strings.isNullOrEmpty(dto.getPostalCode()) || !Strings.isNullOrEmpty(dto.getCity())) {
            ValidationUtils.rejectIfEmpty(errors, "address", "validation.address.notempty.message");
            ValidationUtils.rejectIfEmpty(errors, "postalCode", "validation.address.notempty.message");
            ValidationUtils.rejectIfEmpty(errors, "city", "validation.address.notempty.message");
            if (!StringUtils.isNumeric(dto.getPostalCode()))
                errors.rejectValue("postalCode", "validation.number.postalCode");
        }

        if (!Strings.isNullOrEmpty(dto.getPhone()) && !ValidationHelper.isPhoneMatch(dto.getPhone())) {
            errors.rejectValue("phone", "validation.phone.mismatch");
        }

        if (!Strings.isNullOrEmpty(dto.getMobilePhone()) && !ValidationHelper.isPhoneMatch(dto.getMobilePhone())) {
            errors.rejectValue("mobilePhone", "validation.phone.mismatch");
        }
    }

    private void sanitize(ProfileDTO dto) {
        dto.setCity(GeneralUtils.trim(dto.getCity()));
        dto.setAddress(GeneralUtils.trim(dto.getAddress()));
        dto.setPostalCode(GeneralUtils.trim(dto.getPostalCode()));
        dto.setPhone(GeneralUtils.trim(dto.getPhone()));
        dto.setMobilePhone(GeneralUtils.trim(dto.getMobilePhone()));
    }
}
