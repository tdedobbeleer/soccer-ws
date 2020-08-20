package com.soccer.ws.dto;

import lombok.*;

/**
 * Created by u0090265 on 10.07.17.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ContactDTO {
    private String message;
    private String email;

    public ContactDTO(String message, String email) {
        this.message = message;
        this.email = email;
    }
}
