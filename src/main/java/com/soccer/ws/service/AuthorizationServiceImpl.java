package com.soccer.ws.service;

import com.soccer.ws.exceptions.UnauthorizedAccessException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Comment;
import com.soccer.ws.model.News;
import com.soccer.ws.model.Role;
import org.springframework.stereotype.Service;

/**
 * Created by u0090265 on 9/19/14.
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Override
    public void isAuthorized(Account account, News news) {
        authorize(account, news.getAccount());
    }

    @Override
    public void isAuthorized(Account account, Comment comment) {
        authorize(account, comment.getAccount());
    }

    private void authorize(Account requestingAccount, Account account) {
        if (requestingAccount.getRole() != Role.ADMIN) {
            if (!account.equals(requestingAccount))
                throw new UnauthorizedAccessException(String.format("Object cannot be edited/deleted by account %s",
                        requestingAccount.getUsername()));
        }
    }
}
