package com.soccer.ws.service;

import com.soccer.ws.data.MatchStatisticsObject;
import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Match;

import java.util.List;

/**
 * Created by u0090265 on 6/27/15.
 */
public interface StatisticsService {

    List<MatchStatisticsObject> getGoalsPerPlayerForSeason(long seasonId);

    List<MatchStatisticsObject> getAssistsPerPlayerForSeason(long seasonId);

    List<MatchStatisticsObject> getAssistsFor(Account account, long seasonId);

    List<MatchStatisticsObject> getGoalsFor(Account account, long seasonId);

    AccountStatisticDTO getAccountStatistic(List<Match> matches, Account account, boolean isLoggedIn);
}
