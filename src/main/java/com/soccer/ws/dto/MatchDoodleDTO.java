package com.soccer.ws.dto;

/**
 * Created by u0090265 on 09/09/16.
 */
public class MatchDoodleDTO extends BaseClassDTO {
    private DoodleDTO doodle;
    private String date;
    private String hour;
    private String description;

    public MatchDoodleDTO() {}

    public MatchDoodleDTO(long id, DoodleDTO doodle, String date, String hour, String description) {
        super(id);
        this.doodle = doodle;
        this.date = date;
        this.description = description;
        this.hour = hour;
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
}
