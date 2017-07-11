package com.soccer.ws.local;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.*;
import com.soccer.ws.model.Role;
import com.soccer.ws.model.Season;
import com.soccer.ws.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * Created by u0090265 on 17.06.17.
 */
@Profile("default")
@Component
public class DataPopulator {
    private static final Logger logger = LoggerFactory.getLogger(DataPopulator.class);
    private final AccountService accountService;
    private final NewsService newsService;
    private final MatchesService matchesService;
    private final SeasonService seasonService;
    private final TeamService teamService;
    private final DTOConversionHelper dtoConversionHelper;

    @Autowired
    public DataPopulator(AccountService accountService, NewsService newsService, MatchesService matchesService, SeasonService seasonService, TeamService teamService) {
        this.accountService = accountService;
        this.newsService = newsService;
        this.matchesService = matchesService;
        this.seasonService = seasonService;
        this.teamService = teamService;
        this.dtoConversionHelper = new DTOConversionHelperImpl(null);
    }

    @PostConstruct
    @Transactional
    private void populateDB() {
        logger.info("Default profile detected, populating DB");
        //Create account1
        AccountDTO accountDTO1 = new AccountDTO("test1@test.com", "John", "Doe");
        accountService.register(accountDTO1, "test1");
        accountService.activate(accountDTO1);
        accountService.changeRole(accountDTO1, Role.ADMIN);

        //Create account2
        AccountDTO accountDTO2 = new AccountDTO("test2@test.com", "James", "Doe");
        accountService.register(accountDTO2, "test2");
        accountService.activate(accountDTO2);

        newsService.create(new NewsDTO(null, "test1", "testNews1", "01/01/2016", accountDTO1));
        newsService.create(new NewsDTO(null, "test2", "testNews2", "01/01/2016", accountDTO2));

        Season season = seasonService.create("2016-2017");
        SeasonDTO seasonDTO1 = dtoConversionHelper.convertSeasons(Lists.newArrayList(season)).get(0);

        TeamDTO teamDTO1 = teamService.create(new TeamDTO(null, "SVK", new AddressDTO(null, 3000, "Test straat 1", "Leuven", "https://maps.google.be/maps?q=Korbeekdamstraat 42, +3050+Oud-Heverlee&output=embed")));
        TeamDTO teamDTO2 = teamService.create(new TeamDTO(null, "De Kanaries", new AddressDTO(null, 3000, "Test straat 1", "Leuven", null)));
        TeamDTO teamDTO3 = teamService.create(new TeamDTO(null, "KU Leuven", new AddressDTO(null, 3000, "Test straat 1", "Leuven", null)));

        MatchDTO matchDTO1 = matchesService.createMatch(new MatchDTO(
                null, "01/01/2017", "20:00", teamDTO1, teamDTO2, null, null, seasonDTO1
        ));

        MatchDTO matchDTO2 = matchesService.createMatch(new MatchDTO(
                null, "02/01/2017", "20:00", teamDTO2, teamDTO3, null, null, seasonDTO1
        ));

        MatchDTO matchDTO3 = matchesService.createMatch(new MatchDTO(
                null, "03/01/2017", "20:00", teamDTO1, teamDTO3, null, null, seasonDTO1
        ));


        logger.info("DB population succesful");
    }
}
