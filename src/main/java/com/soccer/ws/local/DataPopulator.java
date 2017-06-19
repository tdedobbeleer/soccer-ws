package com.soccer.ws.local;

import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.dto.NewsDTO;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.MatchesService;
import com.soccer.ws.service.NewsService;
import com.soccer.ws.service.SeasonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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

    @Autowired
    public DataPopulator(AccountService accountService, NewsService newsService, MatchesService matchesService, SeasonService seasonService) {
        this.accountService = accountService;
        this.newsService = newsService;
        this.matchesService = matchesService;
        this.seasonService = seasonService;
    }

    @PostConstruct
    private void populateDB() {
        logger.info("Default profile detected, populating DB");
        //Create account1
        AccountDTO account1 = new AccountDTO("test1@test.com", "John", "Doe");
        accountService.registerAccount(account1, "test1");

        //Create account2
        AccountDTO account2 = new AccountDTO("test2@test.com", "James", "Doe");
        accountService.registerAccount(account2, "test2");

        newsService.create(new NewsDTO(null, "test1", "testNews", "01/01/2016", account1), account1);


        logger.info("DB population succesful");
    }
}
