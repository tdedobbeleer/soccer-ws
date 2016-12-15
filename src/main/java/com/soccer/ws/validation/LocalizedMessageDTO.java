package com.soccer.ws.validation;

import com.google.common.base.Preconditions;

import java.util.Locale;

import static java.util.Locale.ENGLISH;

/**
 * Created by u0090265 on 3/15/16.
 */
public class LocalizedMessageDTO {
    private String messageNl;
    private String messageEn;

    public LocalizedMessageDTO() {
    }

    public LocalizedMessageDTO(String messageNl, String messageEn) {
        this.messageEn = messageEn;
        this.messageNl = messageNl;
    }

    public String getMessageNl() {
        return messageNl;
    }

    public void setMessageNl(String messageNl) {
        this.messageNl = messageNl;
    }

    public String getMessageEn() {
        return messageEn;
    }

    public void setMessageEn(String messageEn) {
        this.messageEn = messageEn;
    }

    public String getMessage(Locale locale) {
        Preconditions.checkNotNull(locale, "Locale should never be null.");
        return locale.equals(ENGLISH) ? getMessageEn() : getMessageNl();
    }

    @Override
    public String toString() {
        return "LocalizedMessageDTO{" +
                "messageNl='" + messageNl + '\'' +
                ", messageEn='" + messageEn + '\'' +
                '}';
    }
}
