package com.soccer.ws.dto;

import com.soccer.ws.data.MatchStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Created by u0090265 on 09/09/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MatchDoodleDTO extends BaseClassDTO {
    private DoodleDTO doodle;
    private String date;
    private String hour;
    private String description;
    private MatchStatusEnum matchStatus;

    public MatchDoodleDTO(UUID id, DoodleDTO doodle, String date, String hour, String description, MatchStatusEnum matchStatus) {
        super(id);
        this.doodle = doodle;
        this.date = date;
        this.hour = hour;
        this.description = description;
        this.matchStatus = matchStatus;
    }
}
