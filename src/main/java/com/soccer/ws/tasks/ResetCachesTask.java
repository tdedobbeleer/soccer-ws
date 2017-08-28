package com.soccer.ws.tasks;

import com.soccer.ws.service.CacheAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by u0090265 on 4/23/15.
 */
@Component
public class ResetCachesTask implements Task {
    private static final Logger log = LoggerFactory.getLogger(ResetCachesTask.class);
    private final CacheAdapter cacheAdapter;

    @Autowired
    public ResetCachesTask(CacheAdapter cacheAdapter) {
        this.cacheAdapter = cacheAdapter;
    }

    @Scheduled(fixedDelay = 3600000, zone = "Europe/Brussels")
    @Override
    public void execute() {
        cacheAdapter.resetMatchesCache();
        log.info("Cleaned up the matches.");
    }
}
