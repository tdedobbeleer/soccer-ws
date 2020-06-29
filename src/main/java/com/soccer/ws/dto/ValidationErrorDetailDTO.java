package com.soccer.ws.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by u0090265 on 2/19/16.
 */
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ValidationErrorDetailDTO extends ErrorDetailDTO {

    private final List<ValidationErrorDTO> validationErrorDTOList;

    @Override
    public String toString() {
        return "ValidationErrorDetailDTO{" +
                "validationErrorDTOList=" + validationErrorDTOList +
                "} " + super.toString();
    }
}
