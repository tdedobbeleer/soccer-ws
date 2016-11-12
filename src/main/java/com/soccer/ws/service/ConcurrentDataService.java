package com.soccer.ws.service;

import com.google.common.util.concurrent.ListenableFuture;
import com.soccer.ws.data.AccountStatistic;
import com.soccer.ws.dto.ActionWrapperDTO;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Team;

import java.util.List;
import java.util.Locale;

/**
 * Created by u0090265 on 9/12/14.
 */
public interface ConcurrentDataService {
    ListenableFuture<List<AccountStatistic>> getAccountStatisticsForSeason(long seasonId);

    ListenableFuture<List<ActionWrapperDTO<MatchDTO>>> getMatchForSeasonActionWrappers(long seasonId, Locale locale,
                                                                                       Account account);

    ListenableFuture<List<ActionWrapperDTO<Team>>> getTeamsActionWrappers(Account account, Locale locale);
}
