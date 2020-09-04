package com.soccer.ws.dto;

import lombok.*;

import java.util.UUID;

/**
 * Created by u0090265 on 10/2/15.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class TeamDTO extends BaseClassDTO {
    private String name;
    private AddressDTO address;

    public TeamDTO(UUID id, String name, AddressDTO address) {
        super(id);
        this.name = name;
        this.address = address;
    }
}
