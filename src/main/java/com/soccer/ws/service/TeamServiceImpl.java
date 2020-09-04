package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.AddressDTO;
import com.soccer.ws.dto.TeamDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Address;
import com.soccer.ws.model.Team;
import com.soccer.ws.persistence.AddressDao;
import com.soccer.ws.persistence.MatchesDao;
import com.soccer.ws.persistence.TeamDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by u0090265 on 5/10/14.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    private static final Logger log = LoggerFactory.getLogger(TeamService.class);
    final
    ConcurrentDataService concurrentDataService;
    private final TeamDao teamDao;
    private final MatchesDao matchesDao;

    private final AddressDao addressDao;

    @Autowired
    public TeamServiceImpl(ConcurrentDataService concurrentDataService, TeamDao teamDao, MatchesDao matchesDao, AddressDao addressDao) {
        this.concurrentDataService = concurrentDataService;
        this.teamDao = teamDao;
        this.matchesDao = matchesDao;
        this.addressDao = addressDao;
    }

    @Override
    public List<Team> getAll() {
        return Lists.newArrayList(teamDao.findAll());
    }

    @Override
    public boolean teamExists(String name) {
        return teamDao.findOneByName(name) != null;
    }

    @Override
    public boolean teamExistsExcludeId(final String name, final UUID id) {
        final Team team = teamDao.findOneByName(name);
        if (team != null) {
            return !team.getId().equals(id);
        }
        return false;
    }

    @Override
    public Team get(UUID id) {
        return teamDao.findById(id).orElse(null);
    }

    @Override
    public TeamDTO create(TeamDTO dto) {
        Team team = new Team();
        updateFromDTO(dto, team);
        Team result = teamDao.save(team);
        dto.setId(result.getId());
        return dto;
    }

    @Override
    public void update(TeamDTO teamDTO) {
        Team team = teamDao.findById(teamDTO.getId()).orElseThrow();
        updateFromDTO(teamDTO, team);
        teamDao.save(team);
    }

    private void updateFromDTO(TeamDTO teamDTO, Team team) {
        //If an existing address is chose, get the address, otherwise create a new one.
        if (teamDTO.getAddress().getId() != null) {
            Address address = addressDao.findById(teamDTO.getAddress().getId()).orElse(null);
            if (address == null)
                throw new ObjectNotFoundException(String.format("Address with id %s not found", teamDTO.getAddress().getId()));
            team.setAddress(address);
        } else {
            team.setAddress(getAddress(teamDTO.getAddress()));
        }
        team.setName(teamDTO.getName());
    }

    private Address getAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setAddress(addressDTO.getAddress());
        address.setCity(addressDTO.getCity());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setGoogleLink(addressDTO.getGoogleLink());
        return address;
    }

    @Override
    public List<TeamDTO> getTeams(boolean isLoggedIn) {
        return concurrentDataService.getTeams(isLoggedIn);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean delete(UUID id, Account a) {
        Team team = teamDao.findById(id).orElse(null);
        if (team == null) return true;
        if (!matchesDao.getMatchesForTeam(team).isEmpty()) {
            return false;
        } else {
            teamDao.delete(team);
            return true;
        }
    }
}
