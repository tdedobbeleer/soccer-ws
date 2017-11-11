package com.soccer.ws.dto;

import com.soccer.ws.data.MatchStatusEnum;

/**
 * Created by u0090265 on 09/09/16.
 */
public class MatchDoodleDTO extends BaseClassDTO {
    private DoodleDTO doodle;
    private String date;
    private String hour;
    private String description;
    private MatchStatusEnum matchStatus;

    public MatchDoodleDTO() {}

    public MatchDoodleDTO(long id, DoodleDTO doodle, String date, String hour, String description, MatchStatusEnum matchStatus) {
        super(id);
        this.doodle = doodle;
        this.date = date;
        this.description = description;
        this.hour = hour;
        this.matchStatus = matchStatus;
    }

    public DoodleDTO getDoodle() {
        return doodle;
    }

    public void setDoodle(DoodleDTO doodle) {
        this.doodle = doodle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public MatchStatusEnum getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(MatchStatusEnum matchStatus) {
        this.matchStatus = matchStatus;
    }
}
