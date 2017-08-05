package com.soccer.ws.dto;

import com.soccer.ws.data.PositionsEnum;

/**
 * Created by u0090265 on 10/06/16.
 */
public class ProfileDTO extends BaseClassDTO {
    private AccountDTO account;
    private String mobilePhone;
    private String phone;
    private PositionsEnum position;
    private String description;
    private AddressDTO address;
    private ImageDTO image;

    public ProfileDTO() {
    }

    public ProfileDTO(Long id, AccountDTO account, String mobilePhone, String phone, PositionsEnum position, String description, AddressDTO address, ImageDTO image) {
        super(id);
        this.account = account;
        this.mobilePhone = mobilePhone;
        this.phone = phone;
        this.position = position;
        this.description = description;
        this.address = address;
        this.image = image;
    }


    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PositionsEnum getPosition() {
        return position;
    }

    public void setPosition(PositionsEnum position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }
}
