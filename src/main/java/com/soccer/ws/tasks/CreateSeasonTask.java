package com.soccer.ws.tasks;

import com.soccer.ws.model.Season;
import com.soccer.ws.persistence.SeasonDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

@Component
public class CreateSeasonTask implements Task {
    private static final Logger log = LoggerFactory.getLogger(CreateSeasonTask.class);

    private final SeasonDao seasonDao;

    @Autowired
    public CreateSeasonTask(SeasonDao seasonDao) {
        this.seasonDao = seasonDao;
    }

    @Scheduled(cron = "0 20 * * 7 SUN", zone = "Europe/Brussels")
    @Override
    public void execute() {
        log.info("Execute createSeasonTask - start");

        String thisYear = Integer.toString(LocalDateTime.now().getYear());
        String nextYear = Integer.toString(LocalDateTime.now().plus(Period.ofYears(1)).getYear());
        Season season = new Season();
        season.setDescription(String.format("%s-%s", thisYear, nextYear));

        Optional<Season> optionalSeason = seasonDao.findAllOrderByDescriptionDesc().stream().filter(s -> s.getDescription().equals(season.getDescription())).findFirst();

        if (optionalSeason.isEmpty()) {
            log.info("Could not find season with description {}. Creating season.", season.getDescription());
            seasonDao.save(season);
        } else {
            log.info("Season with description {} is already created. UUID: {} Skipping creation.", season.getDescription(), optionalSeason.get().getId().toString());
        }

        log.info("Execute createSeasonTask - end");
    }
}
