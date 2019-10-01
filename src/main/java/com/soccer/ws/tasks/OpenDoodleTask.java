package com.soccer.ws.tasks;

import com.soccer.ws.service.MatchesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OpenDoodleTask implements Task {
    private final MatchesService matchesService;

    public OpenDoodleTask(MatchesService matchesService) {
        this.matchesService = matchesService;
    }

    @Override
    @Scheduled(cron = "* * ${random.int[8,18]} * * *", zone = "Europe/Brussels")
    public void execute() {
        matchesService.openNextMatchDoodle();
    }
}
