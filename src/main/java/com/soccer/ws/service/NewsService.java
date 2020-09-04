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
import java.util.UUID;

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

    Comment addNewsComment(UUID newsId, String content, Account account);

    Comment changeNewsComment(UUID commentId, UUID newsId, String content, Account account);

    @Transactional
    void deleteNewsComment(UUID commentId, UUID newsId, Account account);

    News getNewsItem(UUID id);

    List<News> getAll();

    Page<News> getPagedNews(Optional<String> term, int start, int pageSize, Optional<Sort> sort);

    int getNewsCount();

    @Transactional
    void deleteNews(UUID id, Account account);

    void sendNewsEmail(NewsDTO news);
}
