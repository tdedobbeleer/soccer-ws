package com.soccer.ws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * Created by u0090265 on 03/10/16.
 */
public class ReCaptchaResponseDTO {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("error-codes")
    private Collection<String> errorCodes;

    public ReCaptchaResponseDTO() {
    }

    public ReCaptchaResponseDTO(boolean success) {
        this.setSuccess(success);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Collection<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Collection<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "ReCaptchaResponse{" +
                "success=" + success +
                ", errorCodes=" + errorCodes +
                '}';
    }
}
