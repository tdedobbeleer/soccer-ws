package com.soccer.ws.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by u0090265 on 14/06/16.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ErrorDetailDTO {
    private String title;
    private int status;
    private String detail;
    private long timeStamp;
    private String path;
    private String developerMessage;

    public ErrorDetailDTO() {
        //Initialize timestamp;
        timeStamp = new Date().getTime();
    }

    public ErrorDetailDTO(String title, int status, String detail, String path, String developerMessage) {
        this();
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.path = path;
        this.developerMessage = developerMessage;
    }
}
