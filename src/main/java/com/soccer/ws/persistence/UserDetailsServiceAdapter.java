package com.soccer.ws.persistence;

import com.soccer.ws.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * User: Tom De Dobbeleer
 * Date: 12/13/13
 * Time: 3:35 PM
 * Remarks: none
 */

@Service("userDetailsService")
@Transactional(readOnly = false)
public class UserDetailsServiceAdapter implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceAdapter.class);
    private final
    AccountDao accountDao;
    private final
    UserDetailsDao userDetailsDao;

    @Autowired
    public UserDetailsServiceAdapter(AccountDao accountDao, UserDetailsDao userDetailsDao) {
        this.accountDao = accountDao;
        this.userDetailsDao = userDetailsDao;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        logger.info(String.format("Trying to load user %s from Database", username));
        Account account =
                accountDao.findByUsernameAndActiveStatus(username, true);
        if (account == null) {
            throw new UsernameNotFoundException(
                    "No user with username " + username);
        }
        String pw = userDetailsDao.findPasswordByUsername(username);
        return new UserDetailsAdapter(account, pw);
    }
}

