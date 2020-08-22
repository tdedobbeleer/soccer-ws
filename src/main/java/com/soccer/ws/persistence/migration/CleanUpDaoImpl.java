package com.soccer.ws.persistence.migration;

/**
 * Created by u0090265 on 10/1/14.
 */

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CleanUpDaoImpl implements CleanUpDao {
    private static final String DELETE_GOALS = "DELETE FROM goals WHERE id NOT IN (SELECT mg.goals_id FROM " +
            "matches_goals mg)";
    Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void cleanGoals() {
        try {
            log.info("Trying to delete non used goals");
            jdbcTemplate.execute(DELETE_GOALS);
        } catch (Exception e) {
            log.error("Deleting goals not succeeded: " + e.getMessage());
        }
    }
}
