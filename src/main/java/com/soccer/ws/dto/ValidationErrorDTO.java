package com.soccer.ws.dto;

import io.swagger.annotations.ApiModel;

/**
 * Created by u0090265 on 2/19/16.
 */
@ApiModel(value = "ValidationErrorDTO")
public class ValidationErrorDTO {
    private String field;
    private String code;
    private LocalizedMessageDTO localizedMessageDTO;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalizedMessageDTO getLocalizedMessageDTO() {
        return localizedMessageDTO;
    }

    public void setLocalizedMessageDTO(LocalizedMessageDTO localizedMessageDTO) {
        this.localizedMessageDTO = localizedMessageDTO;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "ValidationErrorDTO{" +
                "field='" + field + '\'' +
                ", code='" + code + '\'' +
                ", localizedMessageDTO='" + localizedMessageDTO + '\'' +
                '}';
    }
}
