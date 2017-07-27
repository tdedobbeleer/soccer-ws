package com.soccer.ws.controllers;

import com.soccer.ws.dto.PasswordRecoveryDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.service.PwdRecoveryService;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.PwdRecoveryValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by u0090265 on 08/07/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Password REST api", description = "Password REST operations")
public class PasswordRestController extends AbstractRestController {

    private final PwdRecoveryService pwdRecoveryService;
    private final PwdRecoveryValidator pwdRecoveryValidator;


    @Autowired
    public PasswordRestController(SecurityUtils securityUtils, MessageSource messageSource, PwdRecoveryService pwdRecoveryService, PwdRecoveryValidator pwdRecoveryValidator) {
        super(securityUtils, messageSource);
        this.pwdRecoveryService = pwdRecoveryService;
        this.pwdRecoveryValidator = pwdRecoveryValidator;
    }

    @InitBinder("passwordRecoveryDTO")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(pwdRecoveryValidator);
    }

    @RequestMapping(value = "/accounts/password/recovery", method = RequestMethod.POST)
    @ApiOperation(value = "Get a password recovery code", nickname = "forgotPassword")
    public ResponseEntity postForgotPassword(@RequestBody PasswordRecoveryDTO passwordRecoveryDTO, Locale locale) {
        pwdRecoveryService.setRecoveryCodeAndEmail(passwordRecoveryDTO.getEmail(), locale);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/accounts/password/recovery", method = RequestMethod.PUT)
    @ApiOperation(value = "Set a new password using recovery code", nickname = "useRecoveryCode")
    public ResponseEntity getSeasons(@Valid @RequestBody PasswordRecoveryDTO passwordRecoveryDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(result);
        pwdRecoveryService.checkPwdRecoverCodeAndEmail(passwordRecoveryDTO.getCode(), passwordRecoveryDTO.getPassword(), passwordRecoveryDTO.getCode());
        return ResponseEntity.noContent().build();
    }
}
