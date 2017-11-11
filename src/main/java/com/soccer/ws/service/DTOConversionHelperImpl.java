package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.*;
import com.soccer.ws.model.*;
import com.soccer.ws.utils.GeneralUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

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
                    m.getStatus(),
                    convertMatchPoll(m, isLoggedIn),
                    convertGoals(m.getGoals(), isLoggedIn),
                    convertAddress(m.getHomeTeam().getAddress()),
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
                    match.getStatus(),
                    convertMatchPoll(match, isLoggedIn),
                    convertGoals(match.getGoals(), isLoggedIn),
                    convertAddress(match.getHomeTeam().getAddress()),
                    match.getStatusText(), match.getMatchDoodle() != null, convertSeason(match.getSeason()));
        }
        return null;
    }

    @Override
    public List<AddressDTO> convertAddressList(List<Address> addressList) {
        return addressList.stream().map(this::convertAddress).collect(Collectors.toList());
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
        return new PageDTO<>(matchPollDTOMap, matches.getTotalPages(), matches.hasNext(), matches.hasPrevious(),
                matches.getNumber());

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
        return seasons.stream()
                .map(this::convertSeason)
                .collect(Collectors.toList());
    }

    @Override
    public SeasonDTO convertSeason(Season season) {
        return new SeasonDTO(season.getId(), season.getDescription());
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
            accountDTO.setUsername(isLoggedIn ? account.getUsername() : "");
            accountDTO.setFirstName(account.getFirstName());
            accountDTO.setLastName(isLoggedIn ? account.getLastName() : null);
            accountDTO.setName(isLoggedIn ? account.toString() :
                    GeneralUtils.abbreviateName(account.getFirstName(), account.getLastName()));
            accountDTO.setId(account.getId());
            accountDTO.setActivated(isLoggedIn && account.isActive());
            accountDTO.setRole(isLoggedIn ? account.getRole().name() : "UNKNOWN");
            return accountDTO;
        }
        return null;
    }

    @Override
    public List<AccountDTO> convertAccounts(List<Account> account, boolean isLoggedIn) {
        return account.stream().map(a -> convertAccount(a, isLoggedIn)).collect(Collectors.toList());
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
        return new PageDTO<>(matchDoodleDTOList, match.getTotalPages(), match.hasNext(), match.hasPrevious(), match.getNumber());
    }

    @Override
    public MatchDoodleDTO convertMatchDoodle(Match match, Account account, boolean isAdmin) {
        return new MatchDoodleDTO(match.getId(),
                convertDoodle(match, account, isAdmin),
                match.getStringDate(),
                match.getStringHour(),
                match.getDescription(),
                match.getStatus());
    }

    @Override
    public DoodleDTO convertDoodle(Match match, Account account, boolean isAdmin) {
        Doodle doodle = match.getMatchDoodle();
        if (doodle != null) {
            //Run though all active accounts, check if they are present and convert
            List<PresenceDTO> presenceDTOs = Lists.newArrayList();
            List<PresenceDTO> reserveDTOs = Lists.newArrayList();
            for (Account a : cacheAdapter.getActiveAccounts()) {
                Presence p = doodle.getPresenceFor(a);
                PresenceDTO dto = new PresenceDTO(convertAccount(a, account != null), doodle.getPresenceType(a),
                        getModifiedDateIfNeeded(p),
                        isAdmin || (account != null && match.isActive() && a.equals(account)));
                if (dto.getType().equals(Presence.PresenceType.RESERVE)) {
                    reserveDTOs.add(dto);
                } else {
                    presenceDTOs.add(dto);
                }
            }
            return new DoodleDTO(doodle.getId(),
                    presenceDTOs, reserveDTOs,
                    account == null ? null : new PresenceDTO(convertAccount(account, true), doodle.getPresenceType(account)
                            , null
                            , match.isActive()),
                    doodle.countPresences() + reserveDTOs.size());
        }
        return null;
    }

    private AddressDTO convertAddress(Address address) {
        if (address == null) return null;
        return new AddressDTO(
                address.getId(),
                address.getPostalCode(),
                address.getAddress(),
                address.getCity(),
                address.getGoogleLink()
        );
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
        return new PageDTO<>(newsDTOList, page.getTotalPages(), page.hasNext(), page.hasPrevious(), page.getNumber());
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

    @Override
    public ProfileDTO convertProfile(AccountProfile profile, boolean isLoggedIn) {
        if (profile == null) return null;
        return new ProfileDTO(
                profile.getId(),
                convertAccount(profile.getAccount(), isLoggedIn),
                isLoggedIn ? profile.getMobilePhone() : null,
                isLoggedIn ? profile.getPhone() : null,
                profile.getFavouritePosition(),
                profile.getDescription(),
                isLoggedIn ? convertAddress(profile.getAddress()) : null,
                convertImage(profile.getAvatar()),
                profile.getAccount().getAccountSettings().isSendDoodleNotifications(),
                profile.getAccount().getAccountSettings().isSendNewsNotifications()
        );
    }

    @Override
    public ImageDTO convertImage(Image image) {
        if (image == null) return null;
        return new ImageDTO(image.getImageId(), image.getImageUrl());
    }
}
