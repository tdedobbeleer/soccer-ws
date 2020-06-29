package com.soccer.ws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

/**
 * Created by u0090265 on 03/10/16.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ReCaptchaResponseDTO {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("error-codes")
    private Collection<String> errorCodes;

    public ReCaptchaResponseDTO(boolean success) {
        this.setSuccess(success);
    }

    @Override
    public String toString() {
        return "ReCaptchaResponse{" +
                "success=" + success +
                ", errorCodes=" + errorCodes +
                '}';
    }
}
