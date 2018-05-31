package com.soccer.ws.controllers;

import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.dto.MatchPollDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Match;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.MatchesService;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.CreateMatchValidator;
import com.soccer.ws.validators.UpdateMatchValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Created by u0090265 on 08/07/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Matches REST api", description = "Matches REST operations")
public class MatchesRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MatchesRestController.class);

    private final MatchesService matchesService;
    private final DTOConversionHelper DTOConversionHelper;
    private final UpdateMatchValidator updateMatchValidator;
    private final CreateMatchValidator createMatchValidator;

    public MatchesRestController(SecurityUtils securityUtils, MessageSource messageSource, MatchesService matchesService, DTOConversionHelper dtoConversionHelper, UpdateMatchValidator updateMatchValidator, CreateMatchValidator createMatchValidator) {
        super(securityUtils, messageSource);
        this.matchesService = matchesService;
        DTOConversionHelper = dtoConversionHelper;
        this.updateMatchValidator = updateMatchValidator;
        this.createMatchValidator = createMatchValidator;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/matches", method = RequestMethod.POST)
    public
    @ResponseBody
    @ApiOperation(value = "Create match", nickname = "createMatch")
    MatchDTO createMatch(@Valid @RequestBody MatchDTO dto, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(createMatchValidator, dto, result);
        return matchesService.createMatch(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/matches", method = RequestMethod.PUT)
    public
    @ResponseBody
    @ApiOperation(value = "Update match", nickname = "updateMatch")
    MatchDTO updateMatch(@Valid @RequestBody MatchDTO dto, BindingResult result) throws CustomMethodArgumentNotValidException {
        validate(updateMatchValidator, dto, result);
        return matchesService.update(dto);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/matches/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    @ApiOperation(value = "Delete match", nickname = "deleteMatch")
    ResponseEntity deleteMatch(@PathVariable long id) {
        matchesService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/matches/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    @ApiOperation(value = "Get match", nickname = "getMatch")
    ResponseEntity<MatchDTO> getMatch(@PathVariable long id) {
        Match match = matchesService.get(id);
        return new ResponseEntity<>(DTOConversionHelper.convertMatch(match, isLoggedIn()), HttpStatus.OK);
    }

    @RequestMapping(value = "/matches/season/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    @ApiOperation(value = "Get matches for season", nickname = "matchesForSeason")
    List<MatchDTO> getMatchesForSeason(@PathVariable Long id, Locale locale) {
        return matchesService.getMatchesForSeason(id, isLoggedIn());
    }

    @RequestMapping(value = "/matches/next", method = RequestMethod.GET)
    @ApiOperation(value = "Get next match", nickname = "nextMatchPoll")
    public
    @ResponseBody
    MatchDTO getNextMatch() {
        Match m = matchesService.getLatestMatch();
        return DTOConversionHelper.convertMatch(m, isLoggedIn());
    }

    @RequestMapping(value = "/match/{id}/poll", method = RequestMethod.GET)
    @ApiOperation(value = "Get poll for match", nickname = "MatchPoll")
    public ResponseEntity<MatchPollDTO> getMatchPoll(@PathVariable Long id) {
        Match m = matchesService.get(id);
        if (m == null) throw new ObjectNotFoundException(String.format("Match with id %s not found", id));
        return new ResponseEntity<>(DTOConversionHelper.convertMatchPoll(m, isLoggedIn()), HttpStatus.OK);
    }

    @RequestMapping(value = "/match/latest/poll", method = RequestMethod.GET)
    @ApiOperation(value = "Get poll for match", nickname = "latestMatchPoll")
    public ResponseEntity<MatchPollDTO> getLatestMatchPoll() {
        Match m = matchesService.getLatestMatchWithPoll();
        return new ResponseEntity<>(DTOConversionHelper.convertMatchPoll(m, isLoggedIn()), HttpStatus.OK);
    }
}
