package com.soccer.ws.service;

import com.google.common.collect.Sets;
import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.data.PollStatusEnum;
import com.soccer.ws.model.*;
import com.soccer.ws.persistence.MatchesDao;
import com.soccer.ws.persistence.PollDao;
import com.soccer.ws.utils.GeneralUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * Created by u0090265 on 07/06/16.
 */
@Service
public class PollServiceImpl implements PollService {

    private final MatchesDao matchesDao;
    private final PollDao pollDao;

    @Autowired
    public PollServiceImpl(MatchesDao matchesDao, PollDao pollDao) {
        this.matchesDao = matchesDao;
        this.pollDao = pollDao;
    }

    @Override
    public void setMotmPoll(Match match) {
        if (match.getStatus().equals(MatchStatusEnum.PLAYED)) {
            PlayersPoll playersPoll = new PlayersPoll();
            playersPoll.setStartDate(DateTime.now());
            playersPoll.setEndDate(DateTime.now().plusDays(3));
            playersPoll.setQuestion("Automatically generated: Who will be man of the match?");
            playersPoll.setStatus(PollStatusEnum.OPEN);
            match.setMotmPoll(playersPoll);
            //Set motm poll options
            playersPoll.setOptions(getPlayerOptionsFor(match));
        }
    }

    @Override
    public Poll get(UUID pollId) {
        Poll poll = pollDao.findById(pollId).orElse(null);
        GeneralUtils.throwObjectNotFoundException(poll, pollId, Poll.class);
        return poll;
    }

    @Override
    public Set<IdentityOption> refreshPlayerOptions(UUID matchId) {
        Match match = matchesDao.findById(matchId).orElse(null);
        GeneralUtils.throwObjectNotFoundException(match, matchId, Match.class);
        Set<IdentityOption> options = getPlayerOptionsFor(match);
        match.getMotmPoll().setOptions(options);
        matchesDao.save(match);
        return options;
    }

    @Override
    public Poll reset(UUID id) {
        Poll poll = pollDao.findById(id).orElse(null);
        GeneralUtils.throwObjectNotFoundException(poll, id, Poll.class);
        poll.getVotes().clear();
        return pollDao.save(poll);
    }

    @Override
    @Transactional
    public Poll vote(UUID pollId, Vote vote) {
        //Get poll
        Poll poll = pollDao.findById(pollId).orElse(null);
        GeneralUtils.throwObjectNotFoundException(poll, pollId, Poll.class);
        //Only vote if poll is open
        if (poll.getStatus().equals(PollStatusEnum.OPEN)) {
            //Add vote to poll and make sure poll is added to vote
            vote.setPoll(poll);
            poll.replaceVote(vote);
        }
        return pollDao.save(poll);
    }

    private Set<IdentityOption> getPlayerOptionsFor(Match match) {
        Set<IdentityOption> options = Sets.newConcurrentHashSet();
        if (match.getMotmPoll() != null && match.getMatchDoodle() != null) {
            for (Presence p : match.getMatchDoodle().getPresences()) {
                //Add if player was present
                if (p.isPresent()) options.add(new IdentityOption(p.getAccount().getId()));
            }
        }
        return options;
    }
}
