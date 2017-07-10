package com.soccer.ws.controllers;

import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
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

    @Autowired
    public TeamsRestController(SecurityUtils securityUtils, MessageSource messageSource, TeamService teamService, DTOConversionHelper dtoConversionHelper, TeamValidator teamValidator) {
        super(securityUtils, messageSource);
        this.teamService = teamService;
        this.dtoConversionHelper = dtoConversionHelper;
        this.teamValidator = teamValidator;
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
    public TeamDTO createTeam(@Valid @RequestBody TeamDTO teamDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(result);
        return teamService.create(teamDTO);
    }

    @RequestMapping(value = "/teams", method = RequestMethod.PUT)
    @ApiOperation(value = "Create a new team", nickname = "updateTeam")
    public TeamDTO updateTeam(@Valid @RequestBody TeamDTO teamDTO, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(result);
        teamService.update(teamDTO);
        return teamDTO;
    }

    @RequestMapping(value = "/teams", method = RequestMethod.DELETE)
    @ApiOperation(value = "Create a new team", nickname = "deleteTeam")
    public ResponseEntity deleteTeam(TeamDTO teamDTO) {
        if (!teamService.delete(teamDTO.getId(), getAccountFromSecurity())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
