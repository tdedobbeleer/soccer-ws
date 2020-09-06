package com.soccer.ws.dto;

import com.soccer.ws.data.MatchStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by u0090265 on 10/2/15.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class MatchDTO extends BaseClassDTO {
    @ApiModelProperty(value = "Date when match will be played, formatted dd/mm/YYYY", name = "date")
    private String date;
    @ApiModelProperty(value = "Hour when match will be played, formatted HH:mm", name = "hour")
    private String hour;
    @ApiModelProperty(value = "Name of the hometeam", name = "homeTeam")
    private TeamDTO homeTeam;
    @ApiModelProperty(value = "Name of the awayteam", name = "awayTeam")
    private TeamDTO awayTeam;
    @ApiModelProperty(value = "Goals scored by awayteam", name = "atGoals")
    private Integer atGoals;
    @ApiModelProperty(value = "Goals scored by hometeam", name = "htGoals")
    private Integer htGoals;
    @ApiModelProperty(value = "Match status", name = "status")
    private MatchStatusEnum status = MatchStatusEnum.NOT_PLAYED;
    private AddressDTO address;
    @ApiModelProperty(value = "Status text", name = "statusText")
    private String statusText;
    @ApiModelProperty(value = "Match doodle", name = "doodle")
    private boolean hasDoodle;
    @ApiModelProperty(value = "Match season", name = "season")
    private SeasonDTO season;
    @ApiModelProperty(value = "Motm poll", name = "poll")
    private MatchPollDTO poll;
    @ApiModelProperty(value = "Goals of this match, ordered", name = "goals")
    private List<GoalDTO> goals;

    public MatchDTO(UUID id, String date, String hour, TeamDTO homeTeam, TeamDTO awayTeam, Integer atGoals, Integer
            htGoals, MatchStatusEnum status, MatchPollDTO matchPollDTO, List<GoalDTO> goals, AddressDTO
                            address, String statusText, boolean hasDoodle, SeasonDTO season) {
        super(id);
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
        this.goals = goals;
        this.hasDoodle = hasDoodle;
        this.address = address;
    }

    public MatchDTO(UUID id, String date, String hour, TeamDTO homeTeam, TeamDTO awayTeam, Integer atGoals, Integer
            htGoals, SeasonDTO season) {
        super(id);
        this.date = date;
        this.hour = hour;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.atGoals = atGoals;
        this.htGoals = htGoals;
        this.season = season;
    }
}
