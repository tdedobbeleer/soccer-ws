package com.soccer.ws.service;

import com.soccer.ws.dto.ActionWrapperDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Team;

import java.util.List;
import java.util.Locale;

/**
 * Created by u0090265 on 5/11/14.
 */
public interface TeamService {
    List<Team> getAll();

    boolean teamExists(String name);

    Team getTeam(long id);

    //Team createTeam(CreateAndUpdateTeamForm form);

    //Team updateTeam(CreateAndUpdateTeamForm form);

    List<ActionWrapperDTO<Team>> getTeams(Account account, Locale locale);

    boolean deleteTeam(long id, Account a);
}
