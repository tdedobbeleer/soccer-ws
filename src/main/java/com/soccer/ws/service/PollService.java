package com.soccer.ws.service;

import com.soccer.ws.model.IdentityOption;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Poll;
import com.soccer.ws.model.Vote;

import java.util.Set;

/**
 * Created by u0090265 on 07/06/16.
 */
public interface PollService {
    void setMotmPoll(Match match);

    Poll get(Long pollId);

    Set<IdentityOption> refreshPlayerOptions(Long id);

    Poll reset(Long id);

    Poll vote(Long pollId, Vote vote);
}
