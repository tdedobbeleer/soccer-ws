package com.soccer.ws.dto;

/**
 * Created by u0090265 on 10/06/16.
 */
public class AccountDTO extends DTOBaseClass implements Comparable<AccountDTO> {
    private String name;
    private String firstName;
    private String lastName;
    private String username;

    public AccountDTO() {
    }

    public AccountDTO(String username, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
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

    @Override
    public int compareTo(AccountDTO o) {
        return this.name.compareTo(o.name);
    }
}
