package com.soccer.ws.configuration;

import com.soccer.ws.tasks.*;
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
    WakeUpTask wakeUpTask() {
        return new WakeUpTask();
    }

    @Bean
    PollsTask pollsTask() {
        return new PollsTask();
    }
}
