package com.soccer.ws.dto;

import java.util.List;

/**
 * Created by u0090265 on 2/19/16.
 */
public class ValidationErrorDetailDTO extends ErrorDetailDTO {
    public List<ValidationErrorDTO> getValidationErrorDTOList() {
        return validationErrorDTOList;
    }

    private final List<ValidationErrorDTO> validationErrorDTOList;

    public ValidationErrorDetailDTO(List<ValidationErrorDTO> list) {
        this.validationErrorDTOList = list;
    }

    @Override
    public String toString() {
        return "ValidationErrorDetailDTO{" +
                "validationErrorDTOList=" + validationErrorDTOList +
                "} " + super.toString();
    }
}
