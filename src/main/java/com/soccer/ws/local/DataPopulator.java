package com.soccer.ws.local;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.dto.*;
import com.soccer.ws.model.Role;
import com.soccer.ws.model.Season;
import com.soccer.ws.service.*;
import com.soccer.ws.utils.GeneralUtils;
import org.joda.time.DateTime;
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
    private final DoodleService doodleService;

    @Autowired
    public DataPopulator(AccountService accountService, NewsService newsService, MatchesService matchesService, SeasonService seasonService, TeamService teamService, DoodleService doodleService) {
        this.accountService = accountService;
        this.newsService = newsService;
        this.matchesService = matchesService;
        this.seasonService = seasonService;
        this.teamService = teamService;
        this.doodleService = doodleService;
        this.dtoConversionHelper = new DTOConversionHelperImpl(null);
    }

    @PostConstruct
    @Transactional
    private void populateDB() {
        logger.info("Default profile detected, populating DB");
        //Create account1
        AccountDTO accountDTO1 = accountService.register(new RegistrationDTO("test1@test.com", "John", "Doe", "test1"));
        accountService.changeActivation(accountDTO1.getId(), true);
        accountService.changeRole(accountDTO1, Role.ADMIN);

        //Create account2
        AccountDTO accountDTO2 = accountService.register(new RegistrationDTO("test2@test.com", "James", "Doe", "test2"));
        accountService.changeActivation(accountDTO2.getId(), true);

        //Create account3
        AccountDTO accountDTO3 = accountService.register(new RegistrationDTO("test3@test.com", "Jules", "Doe", "test2"));
        accountService.changeActivation(accountDTO3.getId(), true);

        //Create account4
        AccountDTO accountDTO4 = accountService.register(new RegistrationDTO("test4@test.com", "Jef", "Doe", "test2"));
        accountService.changeActivation(accountDTO4.getId(), true);

        //Create account5
        AccountDTO accountDTO5 = accountService.register(new RegistrationDTO("test5@test.com", "Johnny", "Doe", "test2"));
        accountService.changeActivation(accountDTO5.getId(), true);

        //Create account6
        AccountDTO accountDTO6 = accountService.register(new RegistrationDTO("test6@test.com", "Javier", "Doe", "test2"));
        accountService.changeActivation(accountDTO6.getId(), true);

        newsService.create(new NewsDTO(null, "test1", "testNews1", "01/01/2016", accountDTO1));
        newsService.create(new NewsDTO(null, "test2", "testNews2", "01/01/2016", accountDTO2));

        Season season1 = seasonService.create("2016-2017");
        Season season2 = seasonService.create("2017-2018");
        Season season3 = seasonService.create("2018-2019");
        SeasonDTO seasonDTO1 = dtoConversionHelper.convertSeasons(Lists.newArrayList(season1)).get(0);
        SeasonDTO seasonDTO2 = dtoConversionHelper.convertSeasons(Lists.newArrayList(season2)).get(0);
        SeasonDTO seasonDTO3 = dtoConversionHelper.convertSeasons(Lists.newArrayList(season3)).get(0);

        TeamDTO teamDTO1 = teamService.create(new TeamDTO(null, "SVK", new AddressDTO(null, 3000, "Test straat 1", "Leuven", "https://maps.google.be/maps?q=Korbeekdamstraat 42, +3050+Oud-Heverlee&output=embed")));
        TeamDTO teamDTO2 = teamService.create(new TeamDTO(null, "De Kanaries", new AddressDTO(null, 3000, "Test straat 1", "Leuven", null)));
        TeamDTO teamDTO3 = teamService.create(new TeamDTO(null, "KU Leuven", new AddressDTO(null, 3000, "Test straat 1", "Leuven", null)));

        MatchDTO matchDTO1 = matchesService.createMatch(new MatchDTO(
                null, "01/01/2017", "20:00", teamDTO1, teamDTO2, 1, 3, seasonDTO1
        ));

        matchDTO1.setGoals(ImmutableList.<GoalDTO>builder()
                .add(new GoalDTO(accountDTO1, accountDTO2, 0))
                .add(new GoalDTO(accountDTO2, null, 1))
                .add(new GoalDTO(null, null, 2))
                .build()
        );

        doodleService.changePresence(accountDTO1.getId(), matchDTO1.getId(), true);
        doodleService.changePresence(accountDTO2.getId(), matchDTO1.getId(), true);
        doodleService.changePresence(accountDTO3.getId(), matchDTO1.getId(), true);
        doodleService.changePresence(accountDTO4.getId(), matchDTO1.getId(), true);
        doodleService.changePresence(accountDTO5.getId(), matchDTO1.getId(), true);
        doodleService.changePresence(accountDTO6.getId(), matchDTO1.getId(), true);

        matchDTO1.setStatus(MatchStatusEnum.PLAYED);
        matchesService.update(matchDTO1);

        MatchDTO matchDTO2 = matchesService.createMatch(new MatchDTO(
                null, GeneralUtils.convertToStringDate(DateTime.now().plusDays(3)), "20:00", teamDTO2, teamDTO3, null, null, seasonDTO3
        ));

        MatchDTO matchDTO3 = matchesService.createMatch(new MatchDTO(
                null, GeneralUtils.convertToStringDate(DateTime.now().plusDays(7)), "20:00", teamDTO1, teamDTO3, null, null, seasonDTO3
        ));

        MatchDTO matchDTO4 = matchesService.createMatch(new MatchDTO(
                null, GeneralUtils.convertToStringDate(DateTime.now().plusDays(14)), "20:00", teamDTO3, teamDTO2, null, null, seasonDTO3
        ));

        MatchDTO matchDTO5 = matchesService.createMatch(new MatchDTO(
                null, GeneralUtils.convertToStringDate(DateTime.now().plusDays(14)), "20:00", teamDTO3, teamDTO1, null, null, seasonDTO3
        ));


        Lists.newArrayList(seasonDTO1, seasonDTO2, seasonDTO3).forEach(s -> {
            for (int i = 0; i < 5; i++) {
                matchesService.createMatch(new MatchDTO(
                        null, GeneralUtils.convertToStringDate(DateTime.now().plusDays(i)), "20:00", teamDTO3, teamDTO2, null, null, s));

            }

        });

        logger.info("DB population succesful");
    }
}
