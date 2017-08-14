package com.soccer.ws.dto;

/**
 * Created by u0090265 on 13.08.17.
 */
public class PasswordDTO extends BaseClassDTO {
    private String oldPassword;
    private String newPassword;

    public PasswordDTO() {
    }

    public PasswordDTO(long id, String newPassword, String oldPassword) {
        super(id);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
