package com.soccer.ws.service;

import com.soccer.ws.model.Account;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Presence;

import java.util.Set;
import java.util.UUID;

/**
 * Created by u0090265 on 10/1/14.
 */
public interface DoodleService {
    Presence changePresence(UUID accountId, UUID matchId, boolean isAdmin);

    Presence forceChangePresence(UUID accountId, UUID matchId);

    void sendDoodleNotificationsFor(Match match, Set<Account> accounts);
}
