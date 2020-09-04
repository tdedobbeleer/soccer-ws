package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Season;
import com.soccer.ws.model.Team;
import com.soccer.ws.persistence.MatchesDao;
import com.soccer.ws.persistence.SeasonDao;
import com.soccer.ws.persistence.TeamDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by u0090265 on 9/12/14.
 */
@Service
public class ConcurrentDataServiceImpl implements ConcurrentDataService {
    AccountService accountService;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MatchesDao matchesDao;
    private final TeamDao teamDao;
    private final SeasonDao seasonDao;
    private final StatisticsService statisticsService;
    private final DTOConversionHelper DTOConversionHelper;


    @Autowired
    public ConcurrentDataServiceImpl(MatchesDao matchesDao, TeamDao teamDao, SeasonDao seasonDao, StatisticsService
            statisticsService, AccountService accountService,
                                     DTOConversionHelper DTOConversionHelper) {
        this.matchesDao = matchesDao;
        this.teamDao = teamDao;
        this.seasonDao = seasonDao;
        this.statisticsService = statisticsService;
        this.accountService = accountService;
        this.DTOConversionHelper = DTOConversionHelper;
    }

    @Override
    public List<AccountStatisticDTO> getAccountStatisticsForSeason(final UUID seasonId, final boolean isLoggedIn) {
        Season season = seasonDao.findById(seasonId).orElse(null);
        if (season == null) throw new ObjectNotFoundException(String.format("Season with id %s not found", seasonId));
        List<Match> matches = matchesDao.getMatchesForSeason(season);

        return accountService.getAccountsByActivationStatus(true)
                .parallelStream()
                .map(a -> statisticsService.getAccountStatistic(matches, a, isLoggedIn))
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchDTO> getMatchForSeason(final UUID seasonId, final boolean isLoggedIn) {
        Season season = seasonDao.findById(seasonId).orElseThrow();

        return matchesDao.getMatchesForSeason(season).stream()
                .map(m -> DTOConversionHelper.convertMatch(m, isLoggedIn))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamDTO> getTeams(boolean isLoggedIn) {
        List<Team> teams = Lists.newArrayList(teamDao.findAll());
        return teams.parallelStream()
                .map(t -> DTOConversionHelper.convertTeam(t, isLoggedIn))
                .collect(Collectors.toList());
    }
}
