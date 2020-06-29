package com.soccer.ws.service;

import com.google.common.base.Optional;
import com.soccer.ws.dto.NewsDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Comment;
import com.soccer.ws.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: Tom De Dobbeleer
 * Date: 12/20/13
 * Time: 10:11 AM
 * Remarks: none
 */
public interface NewsService {

    //News createNews(NewsForm form, Account account);

    //void updateNews(NewsForm form, Account account);

    @Transactional
    NewsDTO create(NewsDTO news);

    @Transactional
    void update(NewsDTO news, Account account);

    Comment addNewsComment(long newsId, String content, Account account);

    Comment changeNewsComment(long commentId, long newsId, String content, Account account);

    @Transactional
    void deleteNewsComment(long commentId, long newsId, Account account);

    News getNewsItem(long id);

    List<News> getAll();

    Page<News> getPagedNews(Optional<String> term, int start, int pageSize, Optional<Sort> sort);

    int getNewsCount();

    @Transactional
    void deleteNews(long id, Account account);

    void sendNewsEmail(NewsDTO news);
}
