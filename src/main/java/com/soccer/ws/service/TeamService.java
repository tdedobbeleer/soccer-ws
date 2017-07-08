package com.soccer.ws.service;

import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Team;

import java.util.List;

/**
 * Created by u0090265 on 5/11/14.
 */
public interface TeamService {
    List<Team> getAll();

    boolean teamExists(String name);

    Team get(long id);

    TeamDTO create(TeamDTO dto);

    void update(TeamDTO teamDTO);

    List<TeamDTO> getTeams(Account account);

    boolean deleteTeam(long id, Account a);
}
