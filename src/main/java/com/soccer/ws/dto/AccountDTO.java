package com.soccer.ws.dto;

/**
 * Created by u0090265 on 10/06/16.
 */
public class AccountDTO extends BaseClassDTO implements Comparable<AccountDTO> {
    private String name;
    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private boolean activated;

    public AccountDTO() {
    }

    public AccountDTO(Long id, String username, String firstName, String lastName, String role, boolean activated) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.activated = activated;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int compareTo(AccountDTO o) {
        return this.name.compareTo(o.name);
    }
}
