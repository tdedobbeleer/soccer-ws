package com.soccer.ws.controllers;

import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.dto.MatchPollDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Match;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.MatchesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Locale;

/**
 * Created by u0090265 on 08/07/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Matches REST api", description = "Matches REST operations")
public class MatchesRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MatchesRestController.class);

    @Autowired
    private MatchesService matchesService;

    @Autowired
    private DTOConversionHelper DTOConversionHelper;

    @RequestMapping(value = "/matches/season/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    @ApiOperation(value = "Get matches for season", nickname = "matchesForSeason")
    List<MatchDTO> getMatchesForSeason(@PathVariable Long id, Locale locale) {
        return matchesService.getMatchesForSeason(id, getAccountFromSecurity());
    }

    @RequestMapping(value = "/matches/next", method = RequestMethod.GET)
    public
    @ResponseBody
    MatchDTO getNextMatch() {
        Match m = matchesService.getLatestMatch();
        return DTOConversionHelper.convertMatch(m, isLoggedIn());
    }

    @RequestMapping(value = "/match/{id}/poll", method = RequestMethod.GET)
    @ApiOperation(value = "Get poll for match", nickname = "matchpoll")
    public ResponseEntity<MatchPollDTO> getMatchPoll(@PathVariable Long id) {
        Match m = matchesService.getMatch(id);
        if (m == null) throw new ObjectNotFoundException(String.format("Match with id %s not found", id));
        return new ResponseEntity<>(DTOConversionHelper.convertMatchPoll(m, isLoggedIn()), HttpStatus.OK);
    }

    @RequestMapping(value = "/match/latest/poll", method = RequestMethod.GET)
    @ApiOperation(value = "Get poll for match", nickname = "matchpoll")
    public ResponseEntity<MatchPollDTO> getLatestMatchPoll() {
        Match m = matchesService.getLatestMatchWithPoll();
        return new ResponseEntity<>(DTOConversionHelper.convertMatchPoll(m, isLoggedIn()), HttpStatus.OK);
    }
}
