package com.soccer.ws.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AuthenticationRequestDTO {

    private static final long serialVersionUID = 6624726180748515507L;
    private String username;
    private String password;
    private boolean rememberMe;
}
