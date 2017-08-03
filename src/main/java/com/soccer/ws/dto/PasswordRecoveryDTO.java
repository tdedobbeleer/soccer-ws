package com.soccer.ws.dto;

/**
 * Created by u0090265 on 10.07.17.
 */
public class PasswordRecoveryDTO {
    private String email;
    private String code;
    private String password;

    public PasswordRecoveryDTO() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
