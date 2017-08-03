package com.soccer.ws.dto;

/**
 * Created by u0090265 on 10.07.17.
 */
public class ContactDTO {
    private String message;
    private String email;

    public ContactDTO() {}

    public ContactDTO(String message, String email) {
        this.message = message;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
