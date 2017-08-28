package com.soccer.ws.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

/**
 * Created by u0090265 on 28.08.17.
 */
public class WakeUpTask {
    private static final Logger log = LoggerFactory.getLogger(WakeUpTask.class);
    @Value("${base.url}")
    private String baseUrl;

    @Scheduled(cron = "0 */10 7-23 * * *", zone = "Europe/Brussels")
    public void notIdle() {
        log.info("Making sure application is not idle...");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(baseUrl + "/health", Object.class);
    }
}
