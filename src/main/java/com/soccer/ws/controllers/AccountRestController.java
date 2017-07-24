package com.soccer.ws.controllers;

import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by u0090265 on 09.06.17.
 */
@RestController
public class AccountRestController extends AbstractRestController {
    private final AccountService accountService;
    private final DTOConversionHelper dtoConversionHelper;

    @Autowired
    public AccountRestController(SecurityUtils securityUtils, MessageSource messageSource, AccountService accountService, DTOConversionHelper dtoConversionHelper) {
        super(securityUtils, messageSource);
        this.accountService = accountService;
        this.dtoConversionHelper = dtoConversionHelper;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get Accounts", nickname = "getAccounts")
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        return new ResponseEntity<>(dtoConversionHelper.convertAccounts(accountService.getAll(), isLoggedIn()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts/{id}/activation", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Change activation status", nickname = "changeActivation")
    public ResponseEntity changeActivation(@PathVariable long id, @RequestParam boolean status) {
        accountService.changeActivation(id, status);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts/{id}/elevation", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Elevate user", nickname = "elevate")
    public ResponseEntity elevate(@PathVariable long id) {
        accountService.elevate(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts/{id}/demotion", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Demote user", nickname = "demote")
    public ResponseEntity demote(@PathVariable long id) {
        accountService.demote(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
