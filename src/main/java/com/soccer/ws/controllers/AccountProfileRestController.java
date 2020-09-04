package com.soccer.ws.controllers;

import com.soccer.ws.dto.ProfileDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Role;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.ProfileDTOValidator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by u0090265 on 09.06.17.
 */
@RestController
public class AccountProfileRestController extends AbstractRestController {
    private final AccountService accountService;
    private final DTOConversionHelper dtoConversionHelper;
    private final ProfileDTOValidator profileValidator;

    @Autowired
    public AccountProfileRestController(SecurityUtils securityUtils, MessageSource messageSource, AccountService accountService, DTOConversionHelper dtoConversionHelper, ProfileDTOValidator profileValidator) {
        super(securityUtils, messageSource);
        this.accountService = accountService;
        this.dtoConversionHelper = dtoConversionHelper;
        this.profileValidator = profileValidator;
    }

    @InitBinder("profileDTO")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(profileValidator);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get Account profile", nickname = "getProfile")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable UUID id) {
        final Account account = getAccountFromSecurity();
        if (account.getId().equals(id) || account.getRole().equals(Role.ADMIN)) {
            return new ResponseEntity<>(dtoConversionHelper.convertProfile(accountService.getAccount(id).getAccountProfile(), isLoggedIn()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profiles", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation(value = "Update Account profile", nickname = "updateProfile")
    public ResponseEntity updateProfile(@Valid @RequestBody ProfileDTO profileDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        final Account account = getAccountFromSecurity();
        if (account.getId().equals(profileDTO.getId()) || account.getRole().equals(Role.ADMIN)) {
            validate(result);
            accountService.update(profileDTO);
            return ResponseEntity.noContent().build();

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
    
    @RequestMapping(value = "/profiles", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "Get all Account profiles", nickname = "getAllProfiles")
    public ResponseEntity<List<ProfileDTO>> getProfiles() {
        return new ResponseEntity<>(
                accountService.getAllActivateAccounts().stream().map(a -> dtoConversionHelper.convertProfile(a.getAccountProfile(), isLoggedIn())).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profiles/{id}/image", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "Post image", nickname = "postProfileImage")
    public ResponseEntity postImage(@PathVariable long id, @RequestBody MultipartFile image) {
        return ResponseEntity.noContent().build();
    }

}
