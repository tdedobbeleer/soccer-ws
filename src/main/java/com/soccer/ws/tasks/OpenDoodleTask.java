package com.soccer.ws.tasks;

import com.soccer.ws.service.MatchesService;
import org.springframework.scheduling.annotation.Scheduled;

public class OpenDoodleTask implements Task {
    private final MatchesService matchesService;

    public OpenDoodleTask(MatchesService matchesService) {
        this.matchesService = matchesService;
    }

    @Override
    @Scheduled(cron = "0 0 8 * * 6", zone = "Europe/Brussels")
    public void execute() {
        matchesService.openNextMatchDoodle();
    }
}
