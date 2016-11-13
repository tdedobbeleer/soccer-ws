package com.soccer.ws.service;

import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.model.Account;

import java.util.List;

/**
 * Created by u0090265 on 9/12/14.
 */
public interface ConcurrentDataService {
    List<AccountStatisticDTO> getAccountStatisticsForSeason(long seasonId, Account account);

    List<MatchDTO> getMatchForSeason(long seasonId, Account account);

    List<TeamDTO> getTeams(Account account);
}
