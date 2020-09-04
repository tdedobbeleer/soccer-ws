package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.data.MatchStatisticsObject;
import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.data.PollStatusEnum;
import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by u0090265 on 6/27/15.
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final DTOConversionHelper dtoConversionHelper;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public StatisticsServiceImpl(DTOConversionHelper dtoConversionHelper) {
        this.dtoConversionHelper = dtoConversionHelper;
    }

    @Override
    public List<MatchStatisticsObject> getGoalsPerPlayerForSeason(long seasonId) {
        Query query = em.createNativeQuery("SELECT  C.id as ID, COUNT(B.scorer) as COUNT, " +
                "CONCAT(C.firstName, ' ', C.lastName) as NAME " +
                "FROM matches A LEFT JOIN goals B ON (A.id = B.match_id) " +
                "left join account C ON (B.scorer = C.id) " +
                "where B.scorer IS NOT NULL AND A.season_id = " + seasonId + " group by B.scorer");
        return map(query.getResultList());
    }

    @Override
    public List<MatchStatisticsObject> getAssistsPerPlayerForSeason(long seasonId) {
        Query query = em.createNativeQuery("SELECT C.id as ID, COUNT(B.assist) as COUNT, " +
                "CONCAT(C.firstName, ' ', C.lastName) as NAME " +
                "FROM matches A LEFT JOIN goals B ON (A.id = B.match_id) " +
                "left join account C ON (B.assist = C.id) " +
                "where B.assist IS NOT NULL AND A.season_id = " + seasonId + " group by B.assist");
        return map(query.getResultList());
    }

    @Override
    public List<MatchStatisticsObject> getAssistsFor(Account account, long seasonId) {
        return filterFor(getAssistsPerPlayerForSeason(seasonId), account);
    }

    @Override
    public List<MatchStatisticsObject> getGoalsFor(Account account, long seasonId) {
        return filterFor(getGoalsPerPlayerForSeason(seasonId), account);
    }

    private List<MatchStatisticsObject> filterFor(List<MatchStatisticsObject> oldList, Account account) {
        List<MatchStatisticsObject> newList = Lists.newArrayList();
        for (MatchStatisticsObject o : oldList) {
            if (o.getId().equals(account.getId())) {
                newList.add(o);
            }
        }
        return newList;
    }

    private List<MatchStatisticsObject> map(List<Object[]> result) {
        List<MatchStatisticsObject> l = Lists.newArrayList();
        for (Object[] o : result) {
            MatchStatisticsObject m = new MatchStatisticsObject(Long.parseLong(o[0].toString()), Long.parseLong(o[1]
                    .toString()), (String) o[2]);
            l.add(m);
        }
        return l;
    }

    @Override
    public AccountStatisticDTO getAccountStatistic(List<Match> matches, Account account, boolean isLoggedIn) {
        AccountStatisticDTO accountStatisticDTO = new AccountStatisticDTO(dtoConversionHelper.convertAccount(account, isLoggedIn));
        setMatchData(matches, account, accountStatisticDTO);
        return accountStatisticDTO;
    }

    private void setMatchData(List<Match> matches, Account account, AccountStatisticDTO accountStatisticDTO) {
        int goals = 0;
        int assists = 0;
        int presences = 0;
        int motm = 0;
        for (Match match : matches) {
            //Only gather info on played match
            if (match.getStatus().equals(MatchStatusEnum.PLAYED)) {
                if (match.getMatchDoodle().getPresenceType(account).equals(Presence.PresenceType.PRESENT)) presences++;
                for (Goal goal : match.getGoals()) {
                    if (goal.getScorer() != null && goal.getScorer().equals(account)) {
                        goals++;
                    }
                    if (goal.getAssist() != null && goal.getAssist().equals(account)) {
                        assists++;
                    }
                }

                if (match.getMotmPoll() != null && match.getMotmPoll().getStatus().equals(PollStatusEnum.CLOSED)) {
                    Optional<Ranking<UUID>> rl = match.getMotmPoll().getResult().getHighestRanked();
                    if (rl.isPresent() && rl.get().getOption().equals(account.getId())) {
                        motm++;
                    }

                }
            }
        }
        accountStatisticDTO.setGoals(goals);
        accountStatisticDTO.setAssists(assists);
        accountStatisticDTO.setPlayed(presences);
        accountStatisticDTO.setMotm(motm);
    }
}
