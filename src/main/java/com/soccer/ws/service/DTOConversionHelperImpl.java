package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.*;
import com.soccer.ws.model.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by u0090265 on 10/2/15.
 */
@Service
public class DTOConversionHelperImpl implements DTOConversionHelper {
    private final CacheAdapter cacheAdapter;

    @Autowired
    public DTOConversionHelperImpl(@Lazy CacheAdapter cacheAdapter) {
        this.cacheAdapter = cacheAdapter;
    }

    @Override
    public List<MatchDTO> convertMatches(List<Match> matchList, boolean isLoggedIn) {

        List<MatchDTO> matchDTOs = Lists.newArrayList();
        for (Match m : matchList) {
            matchDTOs.add(new MatchDTO(m.getId(),
                    m.getStringDate(),
                    m.getStringHour(),
                    convertTeam(m.getHomeTeam(), isLoggedIn),
                    convertTeam(m.getAwayTeam(), isLoggedIn),
                    m.getHtGoals(),
                    m.getAtGoals(),
                    m.getStatus().name(),
                    convertMatchPoll(m, isLoggedIn),
                    convertGoals(m.getGoals(), isLoggedIn),
                    m.getHomeTeam().getAddress().getGoogleLink(),
                    m.getHomeTeam().getAddress().getAddress(),
                    m.getStatusText(), m.getMatchDoodle() != null, convertSeason(m.getSeason())));
        }
        return matchDTOs;
    }

    @Override
    public MatchDTO convertMatch(Match match, boolean isLoggedIn) {
        if (match != null) {
            return new MatchDTO(match.getId(),
                    match.getStringDate(),
                    match.getStringHour(),
                    convertTeam(match.getHomeTeam(), isLoggedIn),
                    convertTeam(match.getAwayTeam(), isLoggedIn),
                    match.getAtGoals(),
                    match.getHtGoals(),
                    match.getStatus().name(),
                    convertMatchPoll(match, isLoggedIn),
                    convertGoals(match.getGoals(), isLoggedIn),
                    match.getHomeTeam().getAddress().getGoogleLink(),
                    match.getHomeTeam().getAddress().getAddress(),
                    match.getStatusText(), match.getMatchDoodle() != null, convertSeason(match.getSeason()));
        }
        return null;
    }

    @Override
    public List<TeamDTO> convertTeams(List<Team> teamList, boolean isLoggedIn) {
        List<TeamDTO> teamDTOs = Lists.newArrayList();
        for (Team t : teamList) {
            teamDTOs.add(convertTeam(t, isLoggedIn));
        }
        return teamDTOs;
    }

    @Override
    public TeamDTO convertTeam(Team team, boolean isLoggedIn) {
        return new TeamDTO(team.getId(), team.getName(), convertAddress(team.getAddress()));
    }

    @Override
    public MatchPollDTO convertMatchPoll(Match match, boolean isLoggedIn) {
        PlayersPoll playersPoll = match.getMotmPoll();
        if (playersPoll != null) {
            RankingList<Long> rankingList = playersPoll.getResult();
            return new MatchPollDTO(playersPoll.getId(), match.getId(),
                    convertIdentityRankings(rankingList, isLoggedIn),
                    convertIdentityOptions(playersPoll.getOptions(), isLoggedIn),
                    rankingList.getTotalVotes(), playersPoll.getStatus().name(), match.getDescription(), match
                    .getStringDate());
        }
        return null;
    }

    @Override
    public PageDTO<MatchPollDTO> convertMatchPolls(Page<Match> matches, boolean isLoggedIn) {
        List<MatchPollDTO> matchPollDTOMap = Lists.newLinkedList();
        for (Match m : matches.getContent()) {
            matchPollDTOMap.add(convertMatchPoll(m, isLoggedIn));
        }
        return new PageDTO<>(matchPollDTOMap, matches.getTotalPages(), matches.hasNext(), matches.hasPrevious());

    }

    @Override
    public List<AccountDTO> convertIdentityOptions(Set<IdentityOption> identityOptions, boolean isLoggedIn) {
        List<AccountDTO> accountDTOs = Lists.newArrayList();
        for (IdentityOption account : identityOptions) {
            accountDTOs.add(convertAccount(cacheAdapter.getAccount(account.getOption()), isLoggedIn));
        }
        Collections.sort(accountDTOs);
        return accountDTOs;
    }

    @Override
    public List<VotesDTO> convertIdentityRankings(RankingList<Long> rankingList, boolean isLoggedIn) {
        List<VotesDTO> votes = Lists.newArrayList();
        for (Ranking<Long> ranking : rankingList.getRankings()) {
            votes.add(new VotesDTO(
                    convertAccount(cacheAdapter.getAccount(ranking.getOption()), isLoggedIn),
                    ranking.getPonts()));
        }
        return votes;
    }

    @Override
    public List<SeasonDTO> convertSeasons(List<Season> seasons) {
        List<SeasonDTO> list = Lists.newArrayList();
        for (Season s : seasons) {
            list.add(new SeasonDTO(s.getId(), s.getDescription()));
        }
        return list;
    }

    @Override
    public List<GoalDTO> convertGoals(SortedSet<Goal> goals, boolean isLoggedIn) {
        List<GoalDTO> result = Lists.newArrayList();
        for (Goal g : goals) {
            result.add(new GoalDTO(convertAccount(g.getScorer(), isLoggedIn), convertAccount(g.getAssist(),
                    isLoggedIn), g.getOrder()));
        }
        return result;
    }

    @Override
    public AccountDTO convertAccount(Account account, boolean isLoggedIn) {
        if (account != null) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setName(isLoggedIn ? account.toString() : account.getFullName());
            accountDTO.setId(account.getId());
            return accountDTO;
        }
        return null;
    }

    @Override
    public Account convertAccount(AccountDTO account) {
        if (account != null) {
            Account result = new Account(account.getFirstName(), account.getLastName(), account.getUsername());
            result.setId(account.getId());
            return result;
        }
        return null;
    }

    @Override
    public PageDTO<MatchDoodleDTO> convertMatchDoodles(Page<Match> match, Account account, boolean isAdmin) {
        List<MatchDoodleDTO> matchDoodleDTOList = Lists.newLinkedList();
        for (Match m : match.getContent()) {
            matchDoodleDTOList.add(convertMatchDoodle(m, account, isAdmin));
        }
        return new PageDTO<>(matchDoodleDTOList, match.getTotalPages(), match.hasNext(), match.hasPrevious());
    }

    @Override
    public MatchDoodleDTO convertMatchDoodle(Match match, Account account, boolean isAdmin) {
        return new MatchDoodleDTO(match.getId(),
                convertDoodle(match, account, isAdmin),
                match.getStringDate(),
                match.getDescription());
    }

    @Override
    public DoodleDTO convertDoodle(Match match, Account account, boolean isAdmin) {
        Doodle doodle = match.getMatchDoodle();
        if (doodle != null) {
            //Run though all active accounts, check if they are present and convert
            List<PresenceDTO> presenceDTOs = Lists.newArrayList();
            for (Account a : cacheAdapter.getActiveAccounts()) {
                Presence p = doodle.getPresenceFor(a);
                presenceDTOs.add(new PresenceDTO(convertAccount(a, account != null), doodle.isPresent(a),
                        getModifiedDateIfNeeded(p),
                        isAdmin || (account != null && match.isActive() && a.equals(account))));
            }
            return new DoodleDTO(doodle.getId(),
                    presenceDTOs,
                    account == null ? null : new PresenceDTO(convertAccount(account, true), doodle.isPresent(account)
                            , null
                            , match.isActive()),
                    doodle.countPresences());
        }
        return null;
    }

    private AddressDTO convertAddress(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getPostalCode(),
                address.getAddress(),
                address.getCity(),
                address.getGoogleLink()
        );
    }

    private SeasonDTO convertSeason(Season season) {
        return new SeasonDTO(season.getId(), season.getDescription());
    }

    private String getModifiedDateIfNeeded(Presence presence) {
        if (presence != null)
            if (presence.getModified() != null && presence.getModified().isAfter(DateTime.now().minusDays(2)))
                return presence.getStringModfied();
            else return null;
        else return null;
    }

    @Override
    public PresenceDTO convertPresence(Presence presence, boolean isLoggedIn) {
        if (presence != null) {
            return new PresenceDTO(convertAccount(presence.getAccount(), isLoggedIn), presence.isPresent() ? Presence
                    .PresenceType.PRESENT : Presence.PresenceType.NOT_PRESENT, presence.getStringModfied(), true);
        }
        return null;
    }

    @Override
    public PageDTO<NewsDTO> convertNewsPage(Account account, Page<News> page, boolean isAdmin) {
        List<NewsDTO> newsDTOList = Lists.newLinkedList();
        for (News n : page.getContent()) {
            newsDTOList.add(convertNews(account, n, isAdmin));
        }
        return new PageDTO<>(newsDTOList, page.getTotalPages(), page.hasNext(), page.hasPrevious());
    }

    @Override
    public NewsDTO convertNews(Account account, News news, boolean isAdmin) {
        boolean isLoggedIn = account != null;
        if (news != null) {
            List<CommentDTO> comments = Lists.newArrayList();
            for (Comment comment : news.getComments()) {
                comments.add(convertComment(account, comment, isAdmin, isLoggedIn));
            }
            return new NewsDTO(news.getId(),
                    news.getHeader(),
                    news.getContent(),
                    news.getPostDateString(),
                    convertAccount(news.getAccount(), isLoggedIn),
                    isAdmin || (isLoggedIn && news.getAccount().equals(account)),
                    comments);
        }
        return null;
    }

    @Override
    public CommentDTO convertComment(Account account, Comment comment, boolean isAdmin, boolean isLoggedIn) {
        if (comment != null) {
            return new CommentDTO(comment.getId(), convertAccount(comment.getAccount(), isLoggedIn), comment
                    .getContent(), comment.getPostDateString(),
                    isAdmin || (isLoggedIn && account.equals(comment.getAccount())));
        }
        return null;
    }
}
