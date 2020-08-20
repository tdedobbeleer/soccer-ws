package com.soccer.ws.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by u0090265 on 18.07.17.
 */

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RegistrationDTO {
    private String email, firstName, lastName, password;

    public RegistrationDTO(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + "(" + email + ")";
    }
}
