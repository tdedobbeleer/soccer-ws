package com.soccer.ws.migration.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by u0090265 on 10/24/14.
 */
@Entity
@Table(name = "comment_news")
public class NewsNewComment extends NewComment {
    private NewNews newNews;

    public NewsNewComment() {
        super();
    }

    public NewsNewComment(String content, NewNews newNews, NewAccount account) {
        super(content, account);
        this.newNews = newNews;
    }

    @ManyToOne
    @JoinColumn(name = "NEWS_ID", referencedColumnName = "ID")
    public NewNews getNews() {
        return newNews;
    }

    public void setNews(NewNews newNews) {
        this.newNews = newNews;
    }
}
