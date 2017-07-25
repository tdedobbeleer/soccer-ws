package com.soccer.ws.service;

import com.google.common.base.Optional;
import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.dto.GoalDTO;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Goal;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Season;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.persistence.MatchesDao;
import com.soccer.ws.persistence.SeasonDao;
import com.soccer.ws.persistence.TeamDao;
import com.soccer.ws.utils.GeneralUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by u0090265 on 5/3/14.
 */

@Service
public class MatchesServiceImpl implements MatchesService {

    private static final Logger log = LoggerFactory.getLogger(MatchesService.class);
    private final PollService pollService;
    private final SeasonDao seasonDao;
    private final TeamDao teamDao;
    private final MatchesDao matchesDao;
    private final AccountDao accountDao;
    private final CacheAdapter cacheAdapter;
    @Value("${max.seasons}")
    private int maxSeasons;

    @Autowired
    public MatchesServiceImpl(PollService pollService, SeasonDao seasonDao, TeamDao teamDao, MatchesDao matchesDao, AccountDao accountDao, CacheAdapter cacheAdapter) {
        this.pollService = pollService;
        this.seasonDao = seasonDao;
        this.teamDao = teamDao;
        this.matchesDao = matchesDao;
        this.accountDao = accountDao;
        this.cacheAdapter = cacheAdapter;
    }

    @Override
    public Map<Integer, List<Match>> getMatchesForLastSeasons() {
        int count = 1;
        Map<Integer, List<Match>> resultMap = new HashMap<>();
        for (Season season : seasonDao.findAll(new PageRequest(0, maxSeasons, Sort.Direction.DESC, "description"))) {
            resultMap.put(count, matchesDao.getMatchesForSeason(season));
            count++;
        }
        return resultMap;
    }

    @Override
    public List<Match> getMatchesListForSeason(Season season) {
        return matchesDao.getMatchesForSeason(season);
    }

    @Override
    public Page<Match> getUpcomingMatchesPages(int page, int pageSize, Optional<Sort> sort) {
        DateTime now = DateTime.now();
        Sort s = sort.isPresent() ? sort.get() : new Sort(Sort.Direction.ASC, "date");
        Pageable pageable = new PageRequest(page, pageSize, s);
        return matchesDao.findByDateAfter(now.minusDays(1), pageable);
    }

    @Override
    public List<MatchDTO> getMatchesForSeason(long seasonId, Account
            account) {
        return cacheAdapter.getMatchesForSeason(seasonId, account);
    }


    @Override
    public List<Match> getMatchesForSeason(long seasonId) {
        Season s = seasonDao.findOne(seasonId);
        if (s == null) throw new ObjectNotFoundException(String.format("Season with id %s not found", seasonId));
        return matchesDao.getMatchesForSeason(s);
    }

    @Override
    public Match getMatchByPoll(long pollId) {
        java.util.Optional<Match> m = matchesDao.findByMotmPollId(pollId);
        if (m.isPresent()) {
            return m.get();
        }
        throw new ObjectNotFoundException("Poll with id " + pollId + "not found.");
    }

    @Override
    public List<Match> getMatchesForSeason(String description) {
        Season season = seasonDao.findByDescription(description);
        if (season == null) throw new ObjectNotFoundException(String.format("Season %s does not exists", description));
        return matchesDao.getMatchesForSeason(season);
    }

    @Override
    public Match getLatestMatch() {
        List<Match> matches = matchesDao.findByDate(DateTime.now());
        return matches.isEmpty() ? null : matches.get(0);
    }

    @Override
    public Match getLatestMatchWithPoll() {
        return matchesDao.findFirstByDateBeforeAndMotmPollIsNotNullOrderByDateDesc(DateTime.now());
    }

    @Override
    public Page<Match> getMatchesWithPolls(int page, int pageSize, Optional<Sort> sort, Optional<String> searchTerm) {
        Sort s = sort.isPresent() ? sort.get() : new Sort(Sort.Direction.DESC, "date");
        Pageable pageable = new PageRequest(page, pageSize, s);
        return matchesDao.findByMotmPollNotNull(pageable);
    }

    @Override
    @Transactional(readOnly = false)
    public Match getMatch(long id) {
        return matchesDao.findOne(id);
    }

    @Override
    @Transactional(readOnly = false)
    public MatchDTO createMatch(MatchDTO matchDTO) {
        Match m = new Match();
        m.setSeason(seasonDao.findOne(matchDTO.getSeason().getId()));
        DateTime dateTime = GeneralUtils.convertToDate(matchDTO.getDate(), matchDTO.getHour());
        m.setDate(dateTime);
        m.setHomeTeam(teamDao.findOne(matchDTO.getHomeTeam().getId()));
        m.setAwayTeam(teamDao.findOne(matchDTO.getAwayTeam().getId()));
        matchesDao.save(m);
        log.debug("Match {} created.", m);
        cacheAdapter.resetMatchesCache();
        return matchDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public MatchDTO update(MatchDTO matchDTO) {
        Match m = matchesDao.findOne(matchDTO.getId());
        m.setHomeTeam(teamDao.findOne(matchDTO.getHomeTeam().getId()));
        m.setAwayTeam(teamDao.findOne(matchDTO.getAwayTeam().getId()));
        DateTime dateTime = GeneralUtils.convertToDate(matchDTO.getDate(), matchDTO.getHour());
        m.setDate(dateTime);
        m.setSeason(seasonDao.findOne(matchDTO.getSeason().getId()));
        //Get original match status
        MatchStatusEnum originalMatchStatus = m.getStatus();
        //Set matchstatus
        MatchStatusEnum updatedStatus = matchDTO.getStatus();
        m.setStatus(updatedStatus);
        m.setStatusText(updatedStatus.equals(MatchStatusEnum.CANCELLED) ? matchDTO.getStatusText() : null);

        //If status has changed, check if the motm poll should be added
        if (!updatedStatus.equals(originalMatchStatus)) {
            pollService.setMotmPoll(m);
        }
        if (updatedStatus.equals(MatchStatusEnum.PLAYED)) {
            m.getGoals().clear();
            m.getGoals().addAll(transFormGoals(matchDTO.getGoals(), m));
            m.setAtGoals(matchDTO.getAtGoals());
            m.setHtGoals(matchDTO.getHtGoals());
        } else {
            m.getGoals().clear();
        }
        matchesDao.save(m);
        cacheAdapter.resetMatchesCache();
        cacheAdapter.resetStatisticsCache();
        return matchDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(long id) throws ObjectNotFoundException {
        Match m = matchesDao.findOne(id);
        if (m == null) throw new ObjectNotFoundException(String.format("Match with id %s not found", id));
        matchesDao.delete(id);
        cacheAdapter.resetMatchesCache();
    }


    private List<Goal> transFormGoals(List<GoalDTO> goals, Match match) {
     List<Goal> result = new ArrayList<>();
        for (GoalDTO goalDTO : goals) {
            Goal g = new Goal();
            g.setMatch(match);
            g.setOrder(goalDTO.getOrder());
            //Goals and and assists can be null
            if (goalDTO.getScorer() != null) g.setScorer(accountDao.findOne(goalDTO.getScorer().getId()));
            if (goalDTO.getAssist() != null) g.setAssist(accountDao.findOne(goalDTO.getAssist().getId()));
            result.add(g);
        }
        return result;
     }

}
