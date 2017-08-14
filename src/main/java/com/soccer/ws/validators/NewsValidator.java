package com.soccer.ws.validators;

import com.soccer.ws.dto.NewsDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 10.07.17.
 */
@Component
public class NewsValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NewsDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final NewsDTO dto = (NewsDTO) target;

        ValidationUtils.rejectIfEmpty(errors, "header", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "content", "validation.notempty.message");
    }

    private void sanitize(final NewsDTO newsDTO) {
        newsDTO.setHeader(SanitizeUtils.sanitizeHtml(newsDTO.getHeader()));
        newsDTO.setContent(SanitizeUtils.sanitizeHtml(newsDTO.getContent()));

    }
}
