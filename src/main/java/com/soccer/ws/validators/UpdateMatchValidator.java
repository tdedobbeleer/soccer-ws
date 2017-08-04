package com.soccer.ws.validators;

import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.utils.ValidationHelper;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 5/17/14.
 */
@Component
public class UpdateMatchValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return MatchDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MatchDTO dto = (MatchDTO) o;
        ValidationUtils.rejectIfEmpty(errors, "date", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "homeTeam", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "awayTeam", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "season", "validation.notempty.message");

        if (dto.getStatus().equals(MatchStatusEnum.PLAYED.name())) {
            ValidationUtils.rejectIfEmpty(errors, "atGoals", "validation.notempty.message");
            ValidationUtils.rejectIfEmpty(errors, "htGoals", "validation.notempty.message");
        }

        if (dto.getStatus().equals(MatchStatusEnum.CANCELLED.name())) {
            dto.setStatusText(SanitizeUtils.sanitizeHtml(dto.getStatusText()));
        }

        if (!errors.hasErrors()) {
            if (!ValidationHelper.isValidDate(dto.getDate())) {
                errors.rejectValue("date", "validation.date.wrong", "validation.date.wrong");
            }
            if (!ValidationHelper.isValidTime(dto.getHour())) {
                errors.rejectValue("hour", "validation.hour.wrong", "validation.hour.wrong");
            }
        }
    }
}
