package com.soccer.ws.service;

import com.google.common.base.Optional;
import com.soccer.ws.dto.MatchDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Match;
import com.soccer.ws.model.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by u0090265 on 5/3/14.
 */
public interface MatchesService {

    List<Match> getMatchesListForSeason(Season season);

    Page<Match> getUpcomingMatchesPages(int page, int pageSize, Optional<Sort> sort);

    List<MatchDTO> getMatchesForSeason(long seasonId, boolean isLoggedIn);

    List<Match> getMatchesForSeason(long seasonId);

    Match getMatchByPoll(long pollId);

    List<Match> getMatchesForSeason(String description);

    Match getLatestMatch();

    Match getLatestMatchWithPoll();

    Page<Match> getMatchesWithPolls(int page, int pageSize, Optional<Sort> sort, Optional<String> searchTerm);

    Match get(long id);

    //Match createMatch(CreateMatchForm form) throws ParseException;

    //Match updateMatch(ChangeResultForm form);

    @Transactional(readOnly = false)
    MatchDTO createMatch(MatchDTO matchDTO);

    @Transactional(readOnly = false)
    MatchDTO update(MatchDTO matchDTO);

    void delete(long id) throws ObjectNotFoundException;
}
