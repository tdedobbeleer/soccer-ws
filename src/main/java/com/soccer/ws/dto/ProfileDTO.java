package com.soccer.ws.dto;

import com.soccer.ws.data.PositionsEnum;
import lombok.*;

import java.util.UUID;

/**
 * Created by u0090265 on 10/06/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ProfileDTO extends BaseClassDTO {
    private AccountDTO account;
    private String mobilePhone;
    private String phone;
    private PositionsEnum position;
    private String description;
    private AddressDTO address;
    private ImageDTO image;
    private boolean doodleNotifications;
    private boolean newsNotifications;

    public ProfileDTO(UUID id, AccountDTO account, String mobilePhone, String phone, PositionsEnum position, String description, AddressDTO address, ImageDTO image, boolean doodleNotifications, boolean newsNotifications) {
        super(id);
        this.account = account;
        this.mobilePhone = mobilePhone;
        this.phone = phone;
        this.position = position;
        this.description = description;
        this.address = address;
        this.image = image;
        this.doodleNotifications = doodleNotifications;
        this.newsNotifications = newsNotifications;
    }
}
