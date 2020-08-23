package com.soccer.ws.migration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.soccer.ws.utils.GeneralUtils;
import com.soccer.ws.validators.SanitizeUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


/**
 * User: Tom De Dobbeleer
 * Date: 12/18/13
 * Time: 7:42 PM
 * Remarks: Base class for news items.
 */
@Entity
@Table(name = "news")
public class NewNews extends NewBaseClass {
    private Date postDate = new Date();
    private String header;
    private String content;
    private NewAccount account;
    private List<NewsNewComment> comments;


    public NewNews(String header, String content, NewAccount account) {
        setHeader(header);
        setContent(content);
        this.account = account;
    }

    public NewNews() {
        postDate = new Date();
    }

    @NotNull
    @Column(name = "postdate")
    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Transient
    public String getPostDateString() {
        return GeneralUtils.convertToStringDate(this.postDate);
    }

    @NotNull
    @Size(min = 1)
    @Column(name = "header")
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = SanitizeUtils.sanitizeHtml(header);
    }

    @NotNull
    @Size(min = 1)
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = SanitizeUtils.sanitizeHtml(content);
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posted_by", nullable = false)
    public NewAccount getAccount() {
        return account;
    }

    public void setAccount(NewAccount account) {
        this.account = account;
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "news")
    public List<NewsNewComment> getComments() {
        if (comments == null) comments = Lists.newArrayList();
        return comments;
    }

    public void setComments(List<NewsNewComment> comments) {
        this.comments = comments;
    }
}
