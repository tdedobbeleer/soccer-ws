package com.soccer.ws.service;

import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.model.Account;

import java.util.List;

/**
 * Created by u0090265 on 11/3/14.
 */
public interface CacheAdapter {
    Account getAccount(Long l);

    List<Account> getActiveAccounts();

    List<MatchDTO> getMatchesForSeason(long seasonId, boolean isLoggedIn);

    List<AccountStatisticDTO> getStatisticsForSeason(long seasonId, boolean isLoggedIn);

    void resetStatisticsCache();

    void resetMatchesCache();
}
