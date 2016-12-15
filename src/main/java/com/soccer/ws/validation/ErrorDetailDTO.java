package com.soccer.ws.validation;

import java.util.Date;

/**
 * Created by u0090265 on 2/19/16.
 */
public class ErrorDetailDTO {
    private int code;
    private int status;
    private String detail;
    private long timeStamp;
    private String path;
    private String developerMessage;
    private LocalizedMessageDTO localizedMessageDTO;

    public ErrorDetailDTO() {
        //Initialize timestamp;
        timeStamp = new Date().getTime();
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LocalizedMessageDTO getLocalizedMessageDTO() {
        return localizedMessageDTO;
    }

    public void setLocalizedMessageDTO(final LocalizedMessageDTO localizedMessageDTO) {
        this.localizedMessageDTO = localizedMessageDTO;
    }

    @Override
    public String toString() {
        return "ErrorDetailDTO{" +
                "code='" + code + '\'' +
                ", status=" + status +
                ", detail='" + detail + '\'' +
                ", timeStamp=" + timeStamp +
                ", path='" + path + '\'' +
                ", developerMessage='" + developerMessage + '\'' +
                '}';
    }
}
