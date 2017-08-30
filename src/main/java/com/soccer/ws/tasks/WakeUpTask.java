package com.soccer.ws.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

/**
 * Created by u0090265 on 28.08.17.
 */
//In order to keep the app awake, we need an external job to poll the application.
//@Component
public class WakeUpTask implements Task {
    private static final Logger log = LoggerFactory.getLogger(WakeUpTask.class);
    @Value("${application.url}")
    private String appUrl;

    @Override
    @Scheduled(cron = "0 */1 7-23 * * *", zone = "Europe/Brussels")
    public void execute() {
        log.info("Making sure application is not idle...");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(appUrl + "/health", Object.class);
    }
}
