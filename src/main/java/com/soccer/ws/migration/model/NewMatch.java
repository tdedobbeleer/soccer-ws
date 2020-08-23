package com.soccer.ws.migration.model;

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
public class NewMatch extends NewBaseClass {
    private DateTime date;
    private NewSeason newSeason;
    private NewTeam homeNewTeam;
    private NewTeam awayNewTeam;
    private NewPlayersNewPoll motmPoll;
    private NewNews newNews;
    private Integer atGoals;
    private Integer htGoals;
    private SortedSet<NewGoal> newGoals;
    private NewDoodle matchNewDoodle;
    private MatchStatusEnum status = MatchStatusEnum.NOT_PLAYED; //Default value
    private String StatusText;

    public NewMatch() {
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
    public NewSeason getSeason() {
        return newSeason;
    }

    public void setSeason(NewSeason newSeason) {
        this.newSeason = newSeason;
    }

    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "hometeam_id", insertable = true, updatable = true, nullable = false)
    public NewTeam getHomeTeam() {
        return homeNewTeam;
    }

    public void setHomeTeam(NewTeam homeNewTeam) {
        this.homeNewTeam = homeNewTeam;
    }

    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "awayteam_id", insertable = true, updatable = true, nullable = false)
    public NewTeam getAwayTeam() {
        return awayNewTeam;
    }

    public void setAwayTeam(NewTeam awayNewTeam) {
        this.awayNewTeam = awayNewTeam;
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
    public SortedSet<NewGoal> getGoals() {
        return newGoals;
    }

    public void setGoals(SortedSet<NewGoal> newGoals) {
        this.newGoals = newGoals;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id", insertable = true, updatable = true, nullable = true)
    public NewNews getNews() {
        return newNews;
    }

    public void setNews(NewNews newNews) {
        this.newNews = newNews;
    }

    @Transient
    public String getDescription() {
        return String.format("%s - %s", homeNewTeam.getName(), awayNewTeam.getName());
    }

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "doodle_id", insertable = true, updatable = true, nullable = true)
    public NewDoodle getMatchDoodle() {
        if (matchNewDoodle == null) matchNewDoodle = new NewDoodle();
        return matchNewDoodle;
    }

    public void setMatchDoodle(NewDoodle matchNewDoodle) {
        this.matchNewDoodle = matchNewDoodle;
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
    @JoinColumn(name = "motmpoll_id", insertable = true, updatable = true, nullable = true)
    public NewPlayersNewPoll getMotmPoll() {
        return motmPoll;
    }

    public void setMotmPoll(NewPlayersNewPoll motmPoll) {
        this.motmPoll = motmPoll;
    }

    @Transient
    public boolean isActive() {
        return this.status.equals(MatchStatusEnum.NOT_PLAYED);
    }
}
