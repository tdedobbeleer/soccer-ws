package com.soccer.ws.controllers;

import com.soccer.ws.dto.AddressDTO;
import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.service.AddressService;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.TeamService;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.CreateTeamValidator;
import com.soccer.ws.validators.UpdateTeamValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by u0090265 on 16/09/16.
 */
@RestController
@Api(value = "Teams REST api", description = "Teams REST operations")
public class TeamsRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(TeamsRestController.class);
    private final TeamService teamService;
    private final DTOConversionHelper dtoConversionHelper;
    private final CreateTeamValidator createTeamValidator;
    private final UpdateTeamValidator updateTeamValidator;
    private final AddressService addressService;

    @Autowired
    public TeamsRestController(SecurityUtils securityUtils, MessageSource messageSource, TeamService teamService, DTOConversionHelper dtoConversionHelper, CreateTeamValidator teamValidator, UpdateTeamValidator updateTeamValidator, AddressService addressService) {
        super(securityUtils, messageSource);
        this.teamService = teamService;
        this.dtoConversionHelper = dtoConversionHelper;
        this.createTeamValidator = teamValidator;
        this.updateTeamValidator = updateTeamValidator;
        this.addressService = addressService;
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    @ApiOperation(value = "Get teams", nickname = "getTeams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return new ResponseEntity<>(dtoConversionHelper.convertTeams(teamService.getAll(), isLoggedIn()), HttpStatus.OK);
    }

    @RequestMapping(value = "/teams/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get team by id", nickname = "getTeam")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable UUID id) {
        return new ResponseEntity<>(dtoConversionHelper.convertTeam(teamService.get(id), isLoggedIn()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/teams", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new team", nickname = "createTeam")
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamDTO teamDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(createTeamValidator, teamDTO, result);
        return new ResponseEntity<>(teamService.create(teamDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/teams", method = RequestMethod.PUT)
    @ApiOperation(value = "Update a team", nickname = "updateTeam")
    public ResponseEntity updateTeam(@Valid @RequestBody TeamDTO teamDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(updateTeamValidator, teamDTO, result);
        teamService.update(teamDTO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/teams/{id}",
            method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete a team", nickname = "deleteTeam")
    public ResponseEntity deleteTeam(@PathVariable UUID id) {
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
