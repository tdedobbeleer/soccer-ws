package com.soccer.ws.validators;

import com.soccer.ws.dto.AddressDTO;
import com.soccer.ws.dto.TeamDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 5/11/14.
 */
@Component
public class TeamValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return TeamDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TeamDTO dto = (TeamDTO) o;
        sanitizeAll(dto);

        ValidationUtils.rejectIfEmpty(errors, "teamName", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "address", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "postalCode", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "city", "validation.notempty.message");
    }

    private void sanitizeAll(TeamDTO dto) {
        sanitizeAddress(dto.getAddress());
        dto.setName(SanitizeUtils.sanitizeHtml(dto.getName()));
    }

    private void sanitizeAddress(AddressDTO address) {
        address.setAddress(SanitizeUtils.sanitizeHtml(address.getAddress()));
        address.setCity(SanitizeUtils.sanitizeHtml(address.getCity()));
        address.setGoogleLink(SanitizeUtils.sanitizeHtml(address.getGoogleLink()));
    }
}
