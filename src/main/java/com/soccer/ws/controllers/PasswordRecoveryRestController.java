package com.soccer.ws.controllers;

import com.google.common.base.Strings;
import com.soccer.ws.dto.PasswordRecoveryDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.service.GoogleReCaptchaService;
import com.soccer.ws.service.PwdRecoveryService;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.PwdRecoveryValidator;
import com.soccer.ws.validators.RequestPwdRecoveryValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

/**
 * Created by u0090265 on 08/07/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Password recovery REST api", description = "Password recovery REST operations")
public class PasswordRecoveryRestController extends AbstractRestController {

    private final PwdRecoveryService pwdRecoveryService;
    private final PwdRecoveryValidator pwdRecoveryValidator;
    private final RequestPwdRecoveryValidator requestPwdRecoveryValidator;
    private final GoogleReCaptchaService reCaptchaService;


    @Autowired
    public PasswordRecoveryRestController(SecurityUtils securityUtils, MessageSource messageSource, PwdRecoveryService pwdRecoveryService, PwdRecoveryValidator pwdRecoveryValidator, RequestPwdRecoveryValidator requestPwdRecoveryValidator, GoogleReCaptchaService googleReCaptchaService) {
        super(securityUtils, messageSource);
        this.pwdRecoveryService = pwdRecoveryService;
        this.pwdRecoveryValidator = pwdRecoveryValidator;
        this.requestPwdRecoveryValidator = requestPwdRecoveryValidator;
        this.reCaptchaService = googleReCaptchaService;
    }

    @InitBinder("passwordRecoveryDTO")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(pwdRecoveryValidator);
    }

    @RequestMapping(value = "/accounts/password/recovery", method = RequestMethod.POST)
    @ApiOperation(value = "Get a password recovery code", nickname = "forgotPassword")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Activation code was sent to email"),
            @ApiResponse(code = 404, message = "No account with this email address found"),
            @ApiResponse(code = 400, message = "Email could not be sent for some reason"),
    })
    public ResponseEntity postForgotPassword(@RequestBody PasswordRecoveryDTO passwordRecoveryDTO, BindingResult result, Locale locale) throws CustomMethodArgumentNotValidException {
        validate(requestPwdRecoveryValidator, passwordRecoveryDTO, result);
        pwdRecoveryService.setRecoveryCodeAndEmail(passwordRecoveryDTO.getEmail(), locale);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/accounts/password/recovery", method = RequestMethod.PUT)
    @ApiOperation(value = "Set a new password using recovery code", nickname = "useRecoveryCode")
    public ResponseEntity putRecovery(@RequestBody PasswordRecoveryDTO passwordRecoveryDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(pwdRecoveryValidator, passwordRecoveryDTO, result);
        pwdRecoveryService.checkPwdRecoverCodeAndEmail(passwordRecoveryDTO.getCode(), passwordRecoveryDTO.getPassword(), passwordRecoveryDTO.getCode());
        return ResponseEntity.noContent().build();
    }
}
