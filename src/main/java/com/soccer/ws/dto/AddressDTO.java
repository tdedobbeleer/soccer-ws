package com.soccer.ws.dto;

import com.google.common.base.Strings;
import lombok.*;
import org.springframework.data.annotation.Transient;

import java.util.UUID;

/**
 * Created by u0090265 on 17.02.17.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class AddressDTO extends BaseClassDTO {
    private Integer postalCode;
    private String address;
    private String city;
    private String googleLink;

    public AddressDTO(UUID id, Integer postalCode, String address, String city, String googleLink) {
        super(id);
        this.postalCode = postalCode;
        this.address = address;
        this.city = city;
        this.googleLink = googleLink;
    }

    @Transient
    public boolean isFullAddress() {
        //Only return true if all data is filled in
        return !Strings.isNullOrEmpty(this.address) &&
                !Strings.isNullOrEmpty(this.city) &&
                postalCode != null;
    }
}
