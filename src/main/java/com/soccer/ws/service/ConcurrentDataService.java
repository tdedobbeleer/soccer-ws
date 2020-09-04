package com.soccer.ws.service;

import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.dto.TeamDTO;

import java.util.List;
import java.util.UUID;

/**
 * Created by u0090265 on 9/12/14.
 */
public interface ConcurrentDataService {
    List<AccountStatisticDTO> getAccountStatisticsForSeason(UUID seasonId, boolean isLoggedIn);

    List<MatchDTO> getMatchForSeason(UUID seasonId, boolean isLoggedIn);

    List<TeamDTO> getTeams(boolean isLoggedIn);
}
