package com.soccer.ws.dto;

import lombok.*;

/**
 * Created by u0090265 on 10/06/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class AccountDTO extends BaseClassDTO implements Comparable<AccountDTO> {
    private String name;
    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private boolean activated;

    public AccountDTO(Long id, String username, String firstName, String lastName, String role, boolean activated) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.activated = activated;
    }

    @Override
    public int compareTo(AccountDTO o) {
        return this.name.compareTo(o.name);
    }
}
