package com.soccer.ws.tasks;

import com.google.common.collect.Sets;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.DoodleService;
import com.soccer.ws.service.MatchesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by u0090265 on 8/24/15.
 */
public class DoodleReminderTask implements Task {
    private static final Logger log = LoggerFactory.getLogger(DoodleReminderTask.class);

    @Autowired
    private DoodleService doodleService;

    @Autowired
    private MatchesService matchesService;

    @Autowired
    private AccountService accountService;


    @Scheduled(cron = "0 0 14 ? * TUE,THU", zone = "Europe/Brussels")
    @Override
    public void execute() {
        log.info("Execute DoodleReminderTask - start");
        doodleService.sendDoodleNotificationsFor(matchesService.getLatestMatch(), Sets.newHashSet(accountService
                .getAccountsByActivationStatus(true)));
        log.info("Execute DoodleReminderTask - end");
    }
}
