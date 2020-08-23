package com.soccer.ws.migration.persistence;

import com.soccer.ws.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * User: Tom De Dobbeleer
 * Date: 12/13/13
 * Time: 3:26 PM
 * Remarks: none
 */
@Repository
public class NewJdbcNewUserDetailsDao implements NewUserDetailsDao {
    private static final String FIND_PASSWORD_SQL = "select password from account where username = ?";
    private static final String UPDATE_PASSWORD_SQL = "UPDATE account SET password = :password WHERE ID = :id";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NewJdbcNewUserDetailsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String findPasswordByUsername(String username) {
        return jdbcTemplate.queryForObject(
                FIND_PASSWORD_SQL, new Object[]{username}, String.class);
    }

    public void updatePassword(UUID id, String password) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id).addValue("password", password);
        int i = jdbcTemplate.update(UPDATE_PASSWORD_SQL, namedParameters);
        if (i != 1) throw new ObjectNotFoundException("Account not found, password was not set.");
    }
}
