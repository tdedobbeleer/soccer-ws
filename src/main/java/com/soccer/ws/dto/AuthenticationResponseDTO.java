package com.soccer.ws.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class AuthenticationResponseDTO extends BaseClassDTO {

    private static final long serialVersionUID = -6624726180748515507L;
    private String token;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public AuthenticationResponseDTO(String token, UUID id, String firstName, String lastName, List<String> roles) {
        super(id);
        this.setToken(token);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setRoles(roles);
    }
}
