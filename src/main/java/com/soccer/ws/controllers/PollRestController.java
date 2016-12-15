package com.soccer.ws.controllers;

import com.google.common.base.Optional;
import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.dto.MatchPollDTO;
import com.soccer.ws.dto.MultipleChoiceVoteDTO;
import com.soccer.ws.dto.PageDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.MultipleChoicePlayerVote;
import com.soccer.ws.model.PlayersPoll;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.MatchesService;
import com.soccer.ws.service.PollService;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by u0090265 on 10/2/15.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Poll REST api", description = "Poll REST operations")
public class PollRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(PollRestController.class);

    private final PollService pollService;
    private final MatchesService matchesService;
    private final DTOConversionHelper DTOConversionHelper;

    public PollRestController(SecurityUtils securityUtils, MessageSource messageSource, PollService pollService, MatchesService matchesService, DTOConversionHelper dtoConversionHelper) {
        super(securityUtils, messageSource);
        this.pollService = pollService;
        this.matchesService = matchesService;
        DTOConversionHelper = dtoConversionHelper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/poll/{id}/reset", method = RequestMethod.PUT)
    public ResponseEntity resetPoll(@PathVariable Long id) {
        pollService.reset(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/matchPoll/match/{id}/refresh", method = RequestMethod.PUT)
    public ResponseEntity<List<AccountDTO>> refreshMatchPoll(@PathVariable Long id) {
        return new ResponseEntity<>(DTOConversionHelper.convertIdentityOptions(pollService.refreshPlayerOptions(id),
                isLoggedIn()), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/matchPoll/{id}", method = RequestMethod.GET)
    public ResponseEntity<MatchPollDTO> getMatchPoll(@PathVariable Long id) {
        return new ResponseEntity<>(DTOConversionHelper.convertMatchPoll((PlayersPoll) pollService.get(id),
                isLoggedIn()), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/matchPoll/{id}/vote", method = RequestMethod.POST)
    public ResponseEntity<MultipleChoiceVoteDTO<Long>> postMatchPoll(@PathVariable Long id, @RequestBody
    MultipleChoiceVoteDTO<Long>
            vote) {
        logger.debug(vote.toString());
        Account account = getAccountFromSecurity();
        if (account.getId().equals(vote.getAnswer())) {
            logger.debug("Teapot, selfvote not allowed");
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
        pollService.vote(id, new MultipleChoicePlayerVote(getAccountFromSecurity(), vote.getAnswer()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/matchPoll", method = RequestMethod.GET)
    public ResponseEntity<PageDTO<MatchPollDTO>> getAllMatchPolls(@RequestParam int page, @RequestParam(required =
            false) int size, @RequestParam(required = false) String sort) {
        Page<Match> playersPolls = matchesService.getMatchesWithPolls(page, size, Optional.absent(), Optional
                .absent());
        return new ResponseEntity<>(DTOConversionHelper.convertMatchPolls(playersPolls, isLoggedIn()), HttpStatus.OK);
    }
}
