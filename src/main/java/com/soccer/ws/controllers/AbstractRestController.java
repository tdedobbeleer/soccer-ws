package com.soccer.ws.controllers;

import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validation.ErrorDetailDTO;
import com.soccer.ws.validation.LocalizedMessageDTO;
import com.soccer.ws.validation.ValidationErrorDTO;
import com.soccer.ws.validation.ValidationErrorDetailDTO;
import com.soccer.ws.validators.SanitizeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User: Tom De Dobbeleer
 * Date: 1/20/14
 * Time: 1:43 PM
 * Remarks: none
 */
@RequestMapping("/api/v1")
public abstract class AbstractRestController extends AbstractSecurityController {
    private static final Logger log = LoggerFactory.getLogger(AbstractRestController.class);
    final
    MessageSource messageSource;
    private final Function<FieldError, ValidationErrorDTO> fieldErrorToValidationError = fieldError -> {
        ValidationErrorDTO v = new ValidationErrorDTO();
        v.setField(fieldError.getField());
        v.setCode(fieldError.getCode());
        LocalizedMessageDTO localizedMessage = new LocalizedMessageDTO();
        localizedMessage.setMessageEn(getMessage(fieldError.getCode(), Locale.ENGLISH));
        localizedMessage.setMessageNl(getMessage(fieldError.getCode(), new Locale("nl")));
        v.setLocalizedMessageDTO(localizedMessage);
        log.debug("fieldErrorToValidationError - Transforming fieldError into validationError succesful, returing object", SanitizeUtils.sanitizeHtml(v.toString()));
        return v;
    };

    public AbstractRestController(SecurityUtils securityUtils, MessageSource messageSource) {
        super(securityUtils);
        this.messageSource = messageSource;
    }

    @ExceptionHandler
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        ValidationErrorDetailDTO errorDetail = createErrorDetail(new ValidationErrorDetailDTO(),
                HttpStatus.BAD_REQUEST.value(),
                request,
                e);
        errorDetail.setValidationErrorDTOList(getValidationErrors(e));
        log.warn("handleValidationError - Validation errors found ({}), returning http status {}", errorDetail.getValidationErrorDTOList(), HttpStatus.BAD_REQUEST.name());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    protected void validate(BindingResult result) throws CustomMethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomMethodArgumentNotValidException(result);
        }
    }

    protected void validate(final Validator validator, final Object target, final BindingResult result) throws CustomMethodArgumentNotValidException {
        //Validate first
        validator.validate(target, result);
        //Then check result
        validate(result);
    }

    private <T extends ErrorDetailDTO> T createErrorDetail(T errorDetail, int status, HttpServletRequest request, Exception e) {
        errorDetail.setStatus(status);
        errorDetail.setPath(getRequestURI(request));
        errorDetail.setDetail(e.getMessage());
        errorDetail.setDeveloperMessage(e.getClass().getName());
        return errorDetail;
    }

    private String getRequestURI(HttpServletRequest request) {
        return (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
    }

    private List<ValidationErrorDTO> getValidationErrors(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return fieldErrors
                .stream()
                .map(fieldErrorToValidationError)
                .collect(Collectors.toList());
    }

    private String getMessage(final String messageCode, final Locale locale) {
        try {
            return messageSource.getMessage(messageCode, new Object[]{}, locale);
        } catch (Exception e) {
            return messageCode;
        }
    }
}
