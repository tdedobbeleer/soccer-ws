package com.soccer.ws.controllers;

import com.soccer.ws.dto.ProfileDTO;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by u0090265 on 09.06.17.
 */
@RestController
public class AccountProfileRestController extends AbstractRestController {
    private final AccountService accountService;
    private final DTOConversionHelper dtoConversionHelper;

    @Autowired
    public AccountProfileRestController(SecurityUtils securityUtils, MessageSource messageSource, AccountService accountService, DTOConversionHelper dtoConversionHelper) {
        super(securityUtils, messageSource);
        this.accountService = accountService;
        this.dtoConversionHelper = dtoConversionHelper;
    }

    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get Account profile", nickname = "getProfile")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable long id) {
        return new ResponseEntity<>(dtoConversionHelper.convertProfile(accountService.getAccount(id).getAccountProfile(), isLoggedIn()), HttpStatus.OK);
    }

    @RequestMapping(value = "/profiles", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get all Account profiles", nickname = "getAllProfiles")
    public ResponseEntity<List<ProfileDTO>> getProfiles() {
        return new ResponseEntity<>(
                accountService.getAllActivateAccounts().stream().map(a -> dtoConversionHelper.convertProfile(a.getAccountProfile(), isLoggedIn())).collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
