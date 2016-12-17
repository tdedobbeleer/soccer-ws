package com.soccer.ws.configuration;

import com.soccer.ws.tasks.CleanupTask;
import com.soccer.ws.tasks.DoodleReminderTask;
import com.soccer.ws.tasks.PollsTask;
import com.soccer.ws.tasks.ResetCachesTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by u0090265 on 17/08/16.
 */
@Configuration
@EnableScheduling
public class TaskConfig {
    @Bean
    CleanupTask cleanupTask() {
        return new CleanupTask();
    }

    @Bean
    ResetCachesTask resetCachesTask() {
        return new ResetCachesTask();
    }

    @Bean
    DoodleReminderTask doodleReminderTask() {
        return new DoodleReminderTask();
    }

    @Bean
    PollsTask pollsTask() {
        return new PollsTask();
    }
}
