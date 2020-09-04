package com.soccer.ws.service;

import com.soccer.ws.model.IdentityOption;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Poll;
import com.soccer.ws.model.Vote;

import java.util.Set;
import java.util.UUID;

/**
 * Created by u0090265 on 07/06/16.
 */
public interface PollService {
    void setMotmPoll(Match match);

    Poll get(UUID pollId);

    Set<IdentityOption> refreshPlayerOptions(UUID id);

    Poll reset(UUID id);

    Poll vote(UUID pollId, Vote vote);
}
