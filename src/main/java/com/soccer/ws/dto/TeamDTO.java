package com.soccer.ws.dto;

import lombok.*;

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

    public TeamDTO(Long id, String name, AddressDTO address) {
        super(id);
        this.name = name;
        this.address = address;
    }
}
