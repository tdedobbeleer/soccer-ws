package com.soccer.ws.service;

import com.google.common.base.Optional;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Season;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.persistence.MatchesDao;
import com.soccer.ws.persistence.SeasonDao;
import com.soccer.ws.persistence.TeamDao;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by u0090265 on 5/3/14.
 */

@Service
public class MatchesServiceImpl implements MatchesService {

    private static final Logger log = LoggerFactory.getLogger(MatchesService.class);
    @Autowired
    PollService pollService;
    @Value("${max.seasons}")
    private int maxSeasons;
    @Autowired
    private SeasonDao seasonDao;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private MatchesDao matchesDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private CacheAdapter cacheAdapter;

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

    /**
     * @Override
     * @Transactional(readOnly = false)
     * public Match createMatch(CreateMatchForm form) throws ParseException {
     * Match m = new Match();
     * m.setSeason(seasonDao.findOne(form.getSeason()));
     * m.setDate(form.getDate());
     * m.setHomeTeam(teamDao.findOne(form.getHomeTeam()));
     * m.setAwayTeam(teamDao.findOne(form.getAwayTeam()));
     * matchesDao.save(m);
     * log.debug("Match {} created.", m);
     * cacheAdapter.resetMatchesCache();
     * return m;
     * }
     * @Override
     * @Transactional(readOnly = false)
     * public Match updateMatch(ChangeResultForm form) {
     * Match m = matchesDao.findOne(form.getMatchId());
     * m.setHomeTeam(teamDao.findOne(form.getHomeTeam()));
     * m.setAwayTeam(teamDao.findOne(form.getAwayTeam()));
     * m.setDate(form.getDate());
     * m.setSeason(seasonDao.findOne(form.getSeason()));
     * <p>
     * //Get original match status
     * MatchStatusEnum originalMatchStatus = m.getStatus();
     * //Set matchstatus
     * m.setStatus(form.getStatus());
     * m.setStatusText(form.getStatus().equals(MatchStatusEnum.CANCELLED) ? form.getStatusText() : null);
     * <p>
     * //If status has changed, check if the motm poll should be added
     * if (!form.getStatus().equals(originalMatchStatus)) {
     * pollService.setMotmPoll(m);
     * }
     * <p>
     * if (form.getStatus().equals(MatchStatusEnum.PLAYED)) {
     * m.getGoals().clear();
     * m.getGoals().addAll(transFormGoals(form, m));
     * m.setAtGoals(form.getAtGoals());
     * m.setHtGoals(form.getHtGoals());
     * } else {
     * m.getGoals().clear();
     * }
     * matchesDao.save(m);
     * cacheAdapter.resetMatchesCache();
     * cacheAdapter.resetStatisticsCache();
     * return m;
     * }
     **/

    @Override
    @Transactional(readOnly = false)
    public void deleteMatch(long id) throws ObjectNotFoundException {
        Match m = matchesDao.findOne(id);
        if (m == null) throw new ObjectNotFoundException(String.format("Match with id %s not found", id));
        matchesDao.delete(id);
        cacheAdapter.resetMatchesCache();
    }

    /**
     private List<Goal> transFormGoals(ChangeResultForm form, Match match) {
     List<Goal> result = new ArrayList<>();
     for (ChangeResultForm.FormGoal goal : form.getGoals()) {
     Goal g = new Goal();
     g.setMatch(match);
     g.setOrder(goal.getOrder());
     //Goals and and assists can be null
     if (!Strings.isNullOrEmpty(goal.getScorer())) g.setScorer(accountDao.findOne(GeneralUtils.convertToLong(goal
     .getScorer())));
     if (!Strings.isNullOrEmpty(goal.getAssist())) g.setAssist(accountDao.findOne(GeneralUtils.convertToLong(goal
     .getAssist())));
     result.add(g);
     }
     return result;
     }
     **/
}
