package com.soccer.ws.service;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.soccer.ws.dto.NewsDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Comment;
import com.soccer.ws.model.News;
import com.soccer.ws.model.NewsComment;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.persistence.CommentDao;
import com.soccer.ws.persistence.NewsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.soccer.ws.validators.SanitizeUtils.sanitizeHtml;

/**
 * User: Tom De Dobbeleer
 * Date: 12/20/13
 * Time: 10:10 AM
 * Remarks: none
 */
@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {
    private final NewsDao newsDao;
    private final CommentDao commentDao;
    private final AuthorizationService authorizationService;
    private final MailService mailService;
    private final AccountDao accountDao;
    private final MessageSource messageSource;
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${base.url}")
    private String baseUrl;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, CommentDao commentDao, AuthorizationService authorizationService, MailService mailService, AccountDao accountDao, MessageSource messageSource) {
        this.newsDao = newsDao;
        this.commentDao = commentDao;
        this.authorizationService = authorizationService;
        this.mailService = mailService;
        this.accountDao = accountDao;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional(readOnly = false)
    public News create(NewsDTO news, Account account) {
        News n = new News(sanitizeHtml(news.getHeader()), sanitizeHtml(news.getContent()), account);
        return newsDao.save(n);
    }

    @Override
    @Transactional(readOnly = false)
    public News update(NewsDTO news, Account account) {
        News n = newsDao.findOne(news.getId());
        if (n == null)
            throw new ObjectNotFoundException(String.format("Object news with id %s not found", news.getId()));
        n.setPostDate(new Date());
        return newsDao.save(n);
    }


//    @Override
//    @Transactional(readOnly = false)
//    public News createNews(NewsForm form, Account account) {
//        News n = updateNews(new News(), form, account, true);
//        newsDao.save(n);
//        log.info(String.format("Newsitem %s created by %s", n, account));
//        return n;
//    }
//
//    @Override
//    @Transactional(readOnly = false)
//    public void updateNews(NewsForm form, Account account) {
//        News n = newsDao.findOne(form.getId());
//        if (n == null) throw new ObjectNotFoundException(String.format("News item with id %s not found", form.getId
// ()));
//        authorizationService.isAuthorized(account, n);
//        updateNews(n, form, account, false);
//        log.info(String.format("Newsitem %s updated by %s", n, account));
//        newsDao.save(n);
//
//    }

    @Override
    @Transactional(readOnly = false)
    public News addNewsComment(long newsId, String content, Account account) {
        News news = newsDao.findOne(newsId);
        NewsComment comment = new NewsComment(content, news, account);
        news.getComments().add(comment);
        log.info(String.format("Newscomment %s added by %s", comment, account));
        return newsDao.save(news);
    }

    @Override
    @Transactional(readOnly = false)
    public News changeNewsComment(long commentId, long newsId, String content, Account account) {
        Comment comment = commentDao.findOne(commentId);
        authorizationService.isAuthorized(account, comment);
        comment.setContent(content);
        commentDao.save(comment);
        log.info(String.format("Newscomment %s changed by %s", comment, account));
        return newsDao.findOne(newsId);
    }

    @Override
    @Transactional(readOnly = false)
    public News deleteNewsComment(long commentId, long newsId, Account account) {
        NewsComment comment = (NewsComment) commentDao.findOne(commentId);
        authorizationService.isAuthorized(account, comment);
        News news = newsDao.findOne(newsId);
        news.getComments().remove(comment);
        log.info(String.format("Newscomment %s deleted by by %s", comment, account));
        return newsDao.save(news);
    }

    @Override
    public News getNewsItem(long id) {
        News news = newsDao.findOne(id);
        if (news == null) throw new ObjectNotFoundException(String.format("News item with id %s not found", id));
        return news;
    }

    @Override
    public List<News> getAll() {
        return Lists.newArrayList(newsDao.findAll());
    }

    @Override
    public Page<News> getPagedNews(int start, int pageSize, Optional<Sort> sort) {
        Sort s = sort.isPresent() ? sort.get() : new Sort(Sort.Direction.DESC, "postDate");
        return newsDao.findAll((new PageRequest(start, pageSize, s)));
    }

    @Override
    public int getNewsCount() {
        return (int) newsDao.count();
    }

    @Override
    public Page<News> getSearch(String term, int start, int pageSize, Optional<Sort> sort) {
        Sort s = sort.isPresent() ? sort.get() : new Sort(Sort.Direction.DESC, "postDate");
        return newsDao.getSearch("%" + term + "%", new PageRequest(start, pageSize, s));
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteNews(long id, Account account) {
        News news = newsDao.findOne(id);
        authorizationService.isAuthorized(account, news);
        log.info(String.format("Newsitem %s deleted by %s", news, account));
        newsDao.delete(id);
    }

    @Override
    public boolean sendNewsEmail(NewsDTO news) {
        Set<String> emails = Sets.newHashSet();
        for (Account a : accountDao.findAllByActive(true)) {
            if (a.getAccountSettings().isSendNewsNotifications()) {
                emails.add(a.getUsername());
            }
        }
        String newsItemUrl = String.format("%s/newsItem.html?newsId=%s", baseUrl, news.getId());
        String title = messageSource.getMessage("email.news.title", new String[]{news.getHeader(), news.getPostedBy().getName()}, Locale.ENGLISH);
        String body = messageSource.getMessage("email.news.body", new String[]{news.getContent(), newsItemUrl},
                Locale.ENGLISH);
        return mailService.sendMail(emails, title, body);
    }

    /**
     private News updateNews(News n, NewsForm f, Account a, boolean isNew) {
     n.setHeader(f.getTitle());
     n.setContent(f.getBody());
     //n.setId(Strings.isNullOrEmpty(f.getId()) ? null : Long.parseLong(f.getId()));
     if (isNew) {
     n.setAccount(a);
     }
     return n;
     }
     **/
}
