package com.soccer.ws.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * User: Tom De Dobbeleer
 * Date: 12/13/13
 * Time: 3:26 PM
 * Remarks: none
 */
@Repository
public class JdbcUserDetailsDao implements UserDetailsDao {
    private static final String FIND_PASSWORD_SQL = "select password from account where username = ?";
    @Inject
    private JdbcTemplate jdbcTemplate;

    @Override
    public String findPasswordByUsername(String username) {
        return jdbcTemplate.queryForObject(
                FIND_PASSWORD_SQL, new Object[]{username}, String.class);
    }
}
