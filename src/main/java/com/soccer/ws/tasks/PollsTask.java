package com.soccer.ws.tasks;

import com.soccer.ws.data.PollStatusEnum;
import com.soccer.ws.model.Poll;
import com.soccer.ws.persistence.PollDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by u0090265 on 16/07/16.
 */
@Component
public class PollsTask implements Task {
    private static final Logger log = LoggerFactory.getLogger(PollsTask.class);

    final PollDao pollDao;

    @Autowired
    public PollsTask(PollDao pollDao) {
        this.pollDao = pollDao;
    }

    @Scheduled(fixedDelay = 900000, zone = "Europe/Brussels")
    @Override
    public void execute() {
        log.info("Execute pollsTask - start");
        for (Poll p : pollDao.findByStatus(PollStatusEnum.OPEN)) {
            if (p.getEndDate().isBeforeNow()) {
                p.setStatus(PollStatusEnum.CLOSED);
                pollDao.save(p);
                log.info(String.format("Closed poll id %s with question %s (enddate: %s", p.getId(), p.getQuestion(),
                        p.getEndDate()));
            }
        }
        log.info("Execute pollsTask - end");
    }
}
