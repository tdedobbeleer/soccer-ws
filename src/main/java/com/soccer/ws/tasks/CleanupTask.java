package com.soccer.ws.tasks;

import com.soccer.ws.service.PwdRecoveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by u0090265 on 9/19/14.
 */
@Component
public class CleanupTask implements Task {
    private static final Logger log = LoggerFactory.getLogger(CleanupTask.class);
    private final PwdRecoveryService pwdRecoveryService;

    @Autowired
    public CleanupTask(PwdRecoveryService pwdRecoveryService) {
        this.pwdRecoveryService = pwdRecoveryService;
    }

    @Scheduled(cron = "${activationCodeCleanup.cronSchedule}", zone = "Europe/Brussels")
    @Override
    public void execute() {
        log.info("Execute CleanupTask - start");
        pwdRecoveryService.deleteExpiredCodes();
        log.info("Execute CleanupTask - end");
    }
}
