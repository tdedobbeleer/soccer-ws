package com.soccer.ws.controllers;

import com.google.common.base.Strings;
import com.soccer.ws.dto.RegistrationDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.GoogleReCaptchaService;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by u0090265 on 08/07/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Registration REST api", description = "Registration REST operations")
public class RegistrationRestController extends AbstractRestController {

    private final AccountService accountService;

    private final DTOConversionHelper DTOConversionHelper;

    private final GoogleReCaptchaService reCaptchaService;

    @Autowired
    public RegistrationRestController(SecurityUtils securityUtils, MessageSource messageSource, AccountService accountService, DTOConversionHelper dtoConversionHelper, GoogleReCaptchaService reCaptchaService) {
        super(securityUtils, messageSource);
        this.accountService = accountService;
        DTOConversionHelper = dtoConversionHelper;
        this.reCaptchaService = reCaptchaService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ApiOperation(value = "Create an account", nickname = "createAccount")
    public ResponseEntity createAccount(@Valid @RequestBody RegistrationDTO registrationDTO, @RequestParam String captchaResponse, BindingResult result, HttpServletRequest request) throws CustomMethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomMethodArgumentNotValidException(result);
        }
        if (Strings.isNullOrEmpty(captchaResponse) || !reCaptchaService.isResponseValid(request, captchaResponse)) {
            throw new AccessDeniedException("You are a bot, access denied! In yo Face!");
        }
        accountService.register(registrationDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
