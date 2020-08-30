package com.soccer.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.utils.GeneralUtils;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.SortedSet;

/**
 * User: Tom De Dobbeleer
 * Date: 1/17/14
 * Time: 9:36 PM
 * Remarks: none
 */
@Entity
@Table(name = "matches")
public class Match extends BaseClass {
    private DateTime date;
    private Season season;
    private Team homeTeam;
    private Team awayTeam;
    private PlayersPoll motmPoll;
    private News news;
    private Integer atGoals;
    private Integer htGoals;
    private SortedSet<Goal> goals;
    private Doodle matchDoodle;
    private MatchStatusEnum status = MatchStatusEnum.NOT_PLAYED; //Default value
    private String StatusText;

    public Match() {
    }

    @NotNull
    @Column(name = "date")
    @JsonIgnore
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    @Transient
    public String getStringDate() {
        return GeneralUtils.convertToStringDate(this.date);
    }

    @Transient
    public String getStringHour() {
        return GeneralUtils.convertToStringHour(this.date);
    }

    @Transient
    public String getStringDateTime() {
        return GeneralUtils.convertToStringDateTime(this.date);
    }

    @JsonIgnore
    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id")
    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "hometeam_id", insertable = true, updatable = true, nullable = false)
    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "awayteam_id", insertable = true, updatable = true, nullable = false)
    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    @Column(name = "atGoals")
    public Integer getAtGoals() {
        return atGoals == null ? new Integer(0) : atGoals;
    }

    public void setAtGoals(Integer atGoals) {
        this.atGoals = atGoals;
    }

    @Column(name = "htGoals")
    public Integer getHtGoals() {
        return htGoals == null ? new Integer(0) : htGoals;
    }

    public void setHtGoals(Integer htGoals) {
        this.htGoals = htGoals;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "match")
    @OrderBy("order ASC")
    public SortedSet<Goal> getGoals() {
        return goals;
    }

    public void setGoals(SortedSet<Goal> goals) {
        this.goals = goals;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id", insertable = true, updatable = true, nullable = true)
    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    @Transient
    public String getDescription() {
        return String.format("%s - %s", homeTeam.getName(), awayTeam.getName());
    }

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "doodle_id", insertable = true, updatable = true, nullable = true)
    public Doodle getMatchDoodle() {
        if (matchDoodle == null) matchDoodle = new Doodle();
        return matchDoodle;
    }

    public void setMatchDoodle(Doodle matchDoodle) {
        this.matchDoodle = matchDoodle;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @NotNull
    public MatchStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MatchStatusEnum status) {
        this.status = status;
    }

    @Column(name = "status_text")
    public String getStatusText() {
        return StatusText;
    }

    public void setStatusText(String statusText) {
        StatusText = statusText;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "motmpoll_id")
    public PlayersPoll getMotmPoll() {
        return motmPoll;
    }

    public void setMotmPoll(PlayersPoll motmPoll) {
        this.motmPoll = motmPoll;
    }

    @Transient
    public boolean isActive() {
        return this.status.equals(MatchStatusEnum.NOT_PLAYED);
    }
}
