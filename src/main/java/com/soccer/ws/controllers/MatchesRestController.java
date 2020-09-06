package com.soccer.ws.controllers;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.ByteResponseDTO;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.dto.MatchPollDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Match;
import com.soccer.ws.service.CSVService;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.MatchesService;
import com.soccer.ws.service.VCalendarService;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.CreateMatchValidator;
import com.soccer.ws.validators.UpdateMatchValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

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
    private final CSVService csvService;
    private final VCalendarService vCalendarService;

    public MatchesRestController(SecurityUtils securityUtils, MessageSource messageSource, MatchesService matchesService, DTOConversionHelper dtoConversionHelper, UpdateMatchValidator updateMatchValidator, CreateMatchValidator createMatchValidator, CSVService csvService, VCalendarService vCalendarService) {
        super(securityUtils, messageSource);
        this.matchesService = matchesService;
        DTOConversionHelper = dtoConversionHelper;
        this.updateMatchValidator = updateMatchValidator;
        this.createMatchValidator = createMatchValidator;
        this.csvService = csvService;
        this.vCalendarService = vCalendarService;
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
    ResponseEntity deleteMatch(@PathVariable UUID id) {
        matchesService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/matches/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    @ApiOperation(value = "Get match", nickname = "getMatch")
    ResponseEntity<MatchDTO> getMatch(@PathVariable UUID id) {
        Match match = matchesService.get(id);
        return new ResponseEntity<>(DTOConversionHelper.convertMatch(match, isLoggedIn()), HttpStatus.OK);
    }

    @RequestMapping(value = "/matches/season/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    @ApiOperation(value = "Get matches for season", nickname = "matchesForSeason")
    List<MatchDTO> getMatchesForSeason(@PathVariable UUID id, Locale locale) {
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

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/matches/season/{id}/export")
    @ApiOperation(value = "Export matches for season", nickname = "exportMatches")
    public ByteResponseDTO getExportMatches(@PathVariable UUID id) throws IOException {
        List<List<String>> csvData = Lists.<List<String>>newArrayList(Lists.newArrayList("Date", "Home team", "Away team", "Home team goals", "Away team goals"));
        matchesService.getMatchesForSeason(id, isLoggedIn()).forEach(m -> {
            csvData.add(Lists.newArrayList(m.getDate(), m.getHomeTeam().getName(), m.getAwayTeam().getName(), m.getHtGoals().toString(), m.getAtGoals().toString()));
        });

        return new ByteResponseDTO(csvService.write(csvData).getBytes());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/matches/season/{id}/calendar")
    @ApiOperation(value = "Export matches calendar for season", nickname = "exportMatchesCalendar")
    public ByteResponseDTO getExportMatchesCal(@PathVariable UUID id) throws IOException, ValidationException {
        final Calendar c = vCalendarService.getMatchesCalendar(id);
        final CalendarOutputter o = new CalendarOutputter();
        final StringWriter writer = new StringWriter();
        o.output(c, writer);
        return new ByteResponseDTO(writer.toString().getBytes());
    }

    @RequestMapping(value = "/match/{id}/poll", method = RequestMethod.GET)
    @ApiOperation(value = "Get poll for match", nickname = "MatchPoll")
    public ResponseEntity<MatchPollDTO> getMatchPoll(@PathVariable UUID id) {
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
