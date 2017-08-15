package com.soccer.ws.controllers;

import com.soccer.ws.dto.AddressDTO;
import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.service.AddressService;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.TeamService;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.TeamValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by u0090265 on 16/09/16.
 */
@RestController
@Api(value = "Teams REST api", description = "Teams REST operations")
public class TeamsRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(TeamsRestController.class);
    private final TeamService teamService;
    private final DTOConversionHelper dtoConversionHelper;
    private final TeamValidator teamValidator;
    private final AddressService addressService;

    @Autowired
    public TeamsRestController(SecurityUtils securityUtils, MessageSource messageSource, TeamService teamService, DTOConversionHelper dtoConversionHelper, TeamValidator teamValidator, AddressService addressService) {
        super(securityUtils, messageSource);
        this.teamService = teamService;
        this.dtoConversionHelper = dtoConversionHelper;
        this.teamValidator = teamValidator;
        this.addressService = addressService;
    }

    @InitBinder("teamDTO")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(teamValidator);
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    @ApiOperation(value = "Get teams", nickname = "getTeams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return new ResponseEntity<>(dtoConversionHelper.convertTeams(teamService.getAll(), isLoggedIn()), HttpStatus.OK);
    }

    @RequestMapping(value = "/teams", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new team", nickname = "createTeam")
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamDTO teamDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(result);
        return new ResponseEntity<>(teamService.create(teamDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/teams", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a team", nickname = "updateTeam")
    public ResponseEntity updateTeam(@Valid @RequestBody TeamDTO teamDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(result);
        teamService.update(teamDTO);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/teams/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete a team", nickname = "deleteTeam")
    public ResponseEntity deleteTeam(@PathVariable long id) {
        if (!teamService.delete(id, getAccountFromSecurity())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/teams/addresses", method = RequestMethod.GET)
    @ApiOperation(value = "Get all addresses", nickname = "getTeamAddresses")
    public ResponseEntity<List<AddressDTO>> deleteTeam() {
        return new ResponseEntity<>(dtoConversionHelper.convertAddressList(addressService.getAllAddresses()), HttpStatus.OK);
    }
}
