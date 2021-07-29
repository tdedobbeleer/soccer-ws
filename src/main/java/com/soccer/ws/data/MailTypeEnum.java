package com.soccer.ws.data;

/**
 * Created by u0090265 on 13.10.17.
 */
public enum MailTypeEnum {
    ACTIVATION("account-activation"),
    REGISTRATION("account-registration"),
    DOODLE_REMINDER("doodle-reminder"),
    DOODLE_OPEN("doodle-open"),
    DOODLE_RESERVE("doodle-reserve"),
    MESSAGE("message"),
    TEST("test"),
    PASSWORD_RECOVERY("password-recovery");

    private String value;

    MailTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
