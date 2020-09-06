package com.soccer.ws.persistence;

/**
 * User: Tom De Dobbeleer
 * Date: 12/13/13
 * Time: 3:25 PM
 * Remarks: none
 */
public interface UserDetailsDao {
    String findPasswordByUsernameIgnoreCase(String email);
}
