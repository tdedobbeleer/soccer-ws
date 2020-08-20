package com.soccer.ws.dto;

import lombok.*;

/**
 * Created by u0090265 on 10.07.17.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PasswordRecoveryDTO {
    private String email;
    private String code;
    private String password;
}
