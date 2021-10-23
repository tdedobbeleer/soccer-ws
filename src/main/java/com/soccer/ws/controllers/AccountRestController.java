package com.soccer.ws.controllers;

import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.dto.PasswordDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.CacheAdapter;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.PasswordDTOValidator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Created by u0090265 on 09.06.17.
 */
@RestController
public class AccountRestController extends AbstractRestController {
    private final AccountService accountService;
    private final DTOConversionHelper dtoConversionHelper;
    private final PasswordDTOValidator passwordDTOValidator;
    private final CacheAdapter cacheAdapter;

    @Autowired
    public AccountRestController(SecurityUtils securityUtils, MessageSource messageSource, AccountService accountService, DTOConversionHelper dtoConversionHelper, PasswordDTOValidator passwordDTOValidator, CacheAdapter cacheAdapter) {
        super(securityUtils, messageSource);
        this.accountService = accountService;
        this.dtoConversionHelper = dtoConversionHelper;
        this.passwordDTOValidator = passwordDTOValidator;
        this.cacheAdapter = cacheAdapter;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get Accounts", nickname = "getAccounts")
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        return new ResponseEntity<>(dtoConversionHelper.convertAccounts(accountService.getAll(), isLoggedIn()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get Account", nickname = "getAccount")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable UUID id) {
        return new ResponseEntity<>(dtoConversionHelper.convertAccount(accountService.getAccount(id), isLoggedIn()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts/{id}/activation", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Change activation status", nickname = "changeActivation")
    public ResponseEntity changeActivation(@PathVariable UUID id, @RequestParam boolean status) {
        accountService.changeActivation(id, status);
        //reset cache in order to show account everywhere
        cacheAdapter.reset();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts/{id}/activation/first", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Activate account for the first time", nickname = "firstTimeActivation")
    public ResponseEntity firstTimeActivation(@PathVariable UUID id, @RequestParam boolean sendMail) {
        if (!accountService.firstTimeActivation(id, sendMail)) {
            return new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
        }
        //reset cache in order to show account everywhere
        cacheAdapter.reset();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts/{id}/elevation", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Elevate user", nickname = "elevate")
    public ResponseEntity elevate(@PathVariable UUID id) {
        accountService.elevate(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/accounts/{id}/demotion", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Demote user", nickname = "demote")
    public ResponseEntity demote(@PathVariable UUID id) {
        accountService.demote(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/accounts/password", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Change password", nickname = "changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordDTO passwordDTO, BindingResult bindingResult) throws CustomMethodArgumentNotValidException {
        validate(passwordDTOValidator, passwordDTO, bindingResult);
        accountService.setPasswordFor(passwordDTO.getId(), passwordDTO.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
