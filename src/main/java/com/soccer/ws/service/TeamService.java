package com.soccer.ws.service;

import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Team;

import java.util.List;
import java.util.UUID;

/**
 * Created by u0090265 on 5/11/14.
 */
public interface TeamService {
    List<Team> getAll();

    boolean teamExists(String name);

    boolean teamExistsExcludeId(String name, UUID id);

    Team get(UUID id);

    TeamDTO create(TeamDTO dto);

    void update(TeamDTO teamDTO);

    List<TeamDTO> getTeams(boolean isLoggedIn);

    boolean delete(UUID id, Account a);
}
