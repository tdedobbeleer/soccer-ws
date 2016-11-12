package com.soccer.ws.service;

import com.soccer.ws.model.Account;
import com.soccer.ws.model.Comment;
import com.soccer.ws.model.News;

/**
 * Created by u0090265 on 9/19/14.
 */
public interface AuthorizationService {
    void isAuthorized(Account account, News news);

    void isAuthorized(Account account, Comment comment);
}
