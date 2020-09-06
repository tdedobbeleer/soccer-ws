package com.soccer.ws.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by u0090265 on 08/07/16.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SeasonDTO extends BaseClassDTO {
    private String description;

    public SeasonDTO(String description) {
        super();
        this.description = description;
    }

    public SeasonDTO(UUID id, String description) {
        super(id);
        this.description = description;
    }

    @Override
    public String toString() {
        return "SeasonDTO{" +
                "description='" + description + '\'' +
                "} " + super.toString();
    }
}
