package com.soccer.ws.validators;

import com.soccer.ws.dto.AddressDTO;
import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 5/11/14.
 */
@Component
public class CreateTeamValidator implements Validator {
    private final TeamService teamService;

    @Autowired
    public CreateTeamValidator(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TeamDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TeamDTO dto = (TeamDTO) o;
        sanitizeAll(dto);

        ValidationUtils.rejectIfEmpty(errors, "name", "validation.name.notEmpty");
        ValidationUtils.rejectIfEmpty(errors, "address.address", "validation.address.notEmpty");
        ValidationUtils.rejectIfEmpty(errors, "address.postalCode", "validation.postalCode.notEmpty");
        ValidationUtils.rejectIfEmpty(errors, "address.city", "validation.city.notEmpty");

        if (!errors.hasErrors() && teamService.teamExists(dto.getName())) {
            errors.rejectValue("name", "validation.team.name.exists");
        }
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
