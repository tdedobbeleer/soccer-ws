package com.soccer.ws.migration.persistence;

/**
 * User: Tom De Dobbeleer
 * Date: 12/13/13
 * Time: 3:25 PM
 * Remarks: none
 */
public interface NewUserDetailsDao {
    String findPasswordByUsername(String email);
}
