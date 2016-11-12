package com.soccer.ws.service;

import com.soccer.ws.model.Account;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Presence;

import java.util.Set;

/**
 * Created by u0090265 on 10/1/14.
 */
public interface DoodleService {
    String changeMatchPresence(Account account, long matchId, boolean present);

    Presence changePresence(Account account, long accountId, long doodleId);

    boolean sendDoodleNotificationsFor(Match match, Set<Account> accounts);
}
