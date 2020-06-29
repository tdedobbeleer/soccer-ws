package com.soccer.ws.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * Created by u0090265 on 2/19/16.
 */
@ApiModel(value = "ValidationErrorDTO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ValidationErrorDTO {
    private String field;
    private String code;
    private LocalizedMessageDTO localizedMessageDTO;

    @Override
    public String toString() {
        return "ValidationErrorDTO{" +
                "field='" + field + '\'' +
                ", code='" + code + '\'' +
                ", localizedMessageDTO='" + localizedMessageDTO + '\'' +
                '}';
    }
}
