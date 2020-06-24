package com.soccer.ws.service;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.dto.NewsDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Comment;
import com.soccer.ws.model.News;
import com.soccer.ws.model.NewsComment;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.persistence.CommentDao;
import com.soccer.ws.persistence.NewsDao;
import com.soccer.ws.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

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
    public NewsDTO create(NewsDTO news) {
        Account account = accountDao.findById(news.getPostedBy().getId()).orElse(null);
        if (account == null)
            throw new UsernameNotFoundException("Cannopt post news, user not found");
        News n = newsDao.save(new News(news.getHeader(), news.getContent(), account));
        news.setId(n.getId());
        return news;
    }

    @Override
    @Transactional(readOnly = false)
    public void update(NewsDTO news, Account account) {
        News n = newsDao.findById(news.getId()).orElseThrow();
        if (n == null)
            throw new ObjectNotFoundException(String.format("Object news with id %s not found", news.getId()));
        n.setContent(news.getContent());
        n.setHeader(news.getHeader());
        newsDao.save(n);
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
//        News n = newsDao.findById(form.getId());
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
    public Comment addNewsComment(long newsId, String content, Account account) {
        News news = newsDao.findById(newsId).orElseThrow();
        NewsComment comment = new NewsComment(content, news, account);
        commentDao.save(comment);
        log.info(String.format("Newscomment %s added by %s", comment, account));
        return comment;
    }

    @Override
    @Transactional(readOnly = false)
    public Comment changeNewsComment(long commentId, long newsId, String content, Account account) {
        Comment comment = commentDao.findById(commentId).orElseThrow();
        authorizationService.isAuthorized(account, comment);
        comment.setContent(content);
        commentDao.save(comment);
        log.info(String.format("Newscomment %s changed by %s", comment, account));
        newsDao.findById(newsId);
        return comment;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteNewsComment(long commentId, long newsId, Account account) {
        NewsComment comment = (NewsComment) commentDao.findById(commentId).orElseThrow();
        authorizationService.isAuthorized(account, comment);
        News news = newsDao.findById(newsId).orElseThrow();
        news.getComments().remove(comment);
        log.info(String.format("Newscomment %s deleted by by %s", comment, account));
        newsDao.save(news);
    }

    @Override
    public News getNewsItem(long id) {
        News news = newsDao.findById(id).orElse(null);
        if (news == null) throw new ObjectNotFoundException(String.format("News item with id %s not found", id));
        return news;
    }

    @Override
    public List<News> getAll() {
        return Lists.newArrayList(newsDao.findAll());
    }

    @Override
    public Page<News> getPagedNews(Optional<String> term, int start, int pageSize, Optional<Sort> sort) {
        Sort s = sort.isPresent() ? sort.get() : Sort.by(Sort.Direction.DESC, "postDate");
        if (term.isPresent()) {
            return newsDao.getSearch("%" + sanitizeHtml(term.get()) + "%", PageRequest.of(start, pageSize, s));
        }
        return newsDao.findAll((PageRequest.of(start, pageSize, s)));
    }

    @Override
    public int getNewsCount() {
        return (int) newsDao.count();
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteNews(long id, Account account) {
        News news = newsDao.findById(id).orElseThrow();
        authorizationService.isAuthorized(account, news);
        log.info(String.format("Newsitem %s deleted by %s", news, account));
        newsDao.deleteById(id);
    }

    @Override
    public boolean sendNewsEmail(NewsDTO news) {
        String title = messageSource.getMessage("email.news.title", new String[]{news.getHeader(), news.getPostedBy().getName()}, Locale.ENGLISH);
        accountDao.findAllByActive(true).parallelStream().forEach(a -> {
            boolean result = mailService.sendMail(a.getUsername(), a.getFullName(), title, MailTypeEnum.MESSAGE, ImmutableMap.of("message", news,
                    Constants.EMAIL_BASE_URL_VARIABLE, baseUrl));
            if (!result) {
                log.error("Could not send email to {}", a.getUsername());
            }
        });
        return true;
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
