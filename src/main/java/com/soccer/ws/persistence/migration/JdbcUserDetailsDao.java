package com.soccer.ws.persistence.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * User: Tom De Dobbeleer
 * Date: 12/13/13
 * Time: 3:26 PM
 * Remarks: none
 */
@Repository
public class JdbcUserDetailsDao implements UserDetailsDao {
    private static final String FIND_PASSWORD_SQL = "select password from account where username = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserDetailsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String findPasswordByUsername(String username) {
        return jdbcTemplate.queryForObject(
                FIND_PASSWORD_SQL, new Object[]{username}, String.class);
    }
}
