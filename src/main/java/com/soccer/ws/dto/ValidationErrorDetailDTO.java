package com.soccer.ws.dto;

import java.util.List;

/**
 * Created by u0090265 on 2/19/16.
 */
public class ValidationErrorDetailDTO extends ErrorDetailDTO {
    List<ValidationErrorDTO> validationErrorDTOList;

    public List<ValidationErrorDTO> getValidationErrorDTOList() {
        return validationErrorDTOList;
    }

    public void setValidationErrorDTOList(List<ValidationErrorDTO> validationErrorDTOList) {
        this.validationErrorDTOList = validationErrorDTOList;
    }

    @Override
    public String toString() {
        return "ValidationErrorDetailDTO{" +
                "validationErrorDTOList=" + validationErrorDTOList +
                "} " + super.toString();
    }
}
