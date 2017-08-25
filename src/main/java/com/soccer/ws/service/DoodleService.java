package com.soccer.ws.service;

import com.soccer.ws.model.Account;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Presence;

import java.util.Set;

/**
 * Created by u0090265 on 10/1/14.
 */
public interface DoodleService {
    Presence changePresence(long accountId, long matchId, boolean isAdmin);

    boolean sendDoodleNotificationsFor(Match match, Set<Account> accounts);
}
