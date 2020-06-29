package com.soccer.ws.dto;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

import static java.util.Locale.ENGLISH;

/**
 * Created by u0090265 on 3/15/16.
 */
@ApiModel(value = "LocalizedMessageDTO")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LocalizedMessageDTO {
    private String messageNl;
    private String messageEn;

    public LocalizedMessageDTO(String messageNl, String messageEn) {
        this.messageEn = messageEn;
        this.messageNl = messageNl;
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
