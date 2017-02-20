package com.soccer.ws.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by u0090265 on 10/2/15.
 */
public class MatchDTO extends DTOBaseClass {
    private String date;
    private String hour;
    private TeamDTO homeTeam;
    private TeamDTO awayTeam;
    private Integer atGoals;
    private Integer htGoals;
    private String status;
    private String locationUrl;
    private String fullAddress;
    private String statusText;
    private boolean hasDoodle;
    private SeasonDTO season;
    private MatchPollDTO poll;
    private List<GoalDTO> goals;

    public MatchDTO(Long id, String date, String hour, TeamDTO homeTeam, TeamDTO awayTeam, Integer atGoals, Integer
            htGoals, String status, MatchPollDTO matchPollDTO, List<GoalDTO> goals, String locationUrl, String
                            address, String statusText, boolean hasDoodle, SeasonDTO season) {
        this.date = date;
        this.hour = hour;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.atGoals = atGoals;
        this.htGoals = htGoals;
        this.poll = matchPollDTO;
        this.status = status;
        this.statusText = statusText;
        this.season = season;
        this.setId(id);
        this.goals = goals;
        this.hasDoodle = hasDoodle;
        this.locationUrl = locationUrl;
        this.fullAddress = address;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @ApiModelProperty(value = "Goals of this match, orederd", name = "goals")
    public List<GoalDTO> getGoals() {
        return goals;
    }

    public void setGoals(List<GoalDTO> goals) {
        this.goals = goals;
    }


    @ApiModelProperty(value = "Date when match will be played, formatted dd/mm/YYYY", name = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @ApiModelProperty(value = "Hour when match will be played, formatted HH:mm", name = "hour")
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @ApiModelProperty(value = "Name of the hometeam", name = "homeTeam")
    public TeamDTO getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamDTO homeTeam) {
        this.homeTeam = homeTeam;
    }

    @ApiModelProperty(value = "Name of the awayteam", name = "awayTeam")
    public TeamDTO getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(TeamDTO awayTeam) {
        this.awayTeam = awayTeam;
    }

    @ApiModelProperty(value = "Goals scored by awayteam", name = "atGoals")
    public Integer getAtGoals() {
        return atGoals;
    }

    public void setAtGoals(Integer atGoals) {
        this.atGoals = atGoals;
    }

    @ApiModelProperty(value = "Goals scored by hometeam", name = "htGoals")
    public Integer getHtGoals() {
        return htGoals;
    }

    public void setHtGoals(Integer htGoals) {
        this.htGoals = htGoals;
    }

    @ApiModelProperty(value = "Motm poll", name = "poll")
    public MatchPollDTO getPoll() {
        return poll;
    }

    public void setPoll(MatchPollDTO poll) {
        this.poll = poll;
    }

    @ApiModelProperty(value = "Match status", name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ApiModelProperty(value = "Match location", name = "location")
    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    @ApiModelProperty(value = "Match doodle", name = "doodle")
    public boolean isHasDoodle() {
        return hasDoodle;
    }

    public void setHasDoodle(boolean hasDoodle) {
        this.hasDoodle = hasDoodle;
    }

    @ApiModelProperty(value = "Status text", name = "statusText")
    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public SeasonDTO getSeason() {
        return season;
    }

    public void setSeason(SeasonDTO season) {
        this.season = season;
    }
}
