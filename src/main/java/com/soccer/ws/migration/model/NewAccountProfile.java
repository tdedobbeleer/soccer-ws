package com.soccer.ws.migration.model;

import com.soccer.ws.data.PositionsEnum;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by u0090265 on 11/8/15.
 */
@Entity
@Table(name = "account_profile")
public class NewAccountProfile extends NewBaseClass {
    private NewAccount account;
    private NewImage avatar;
    private String mobilePhone;
    private String phone;
    private PositionsEnum favouritePosition;
    private String description;
    private NewAddress newAddress;

    @OneToOne(mappedBy = "accountProfile")
    public NewAccount getAccount() {
        return account;
    }

    public void setAccount(NewAccount account) {
        this.account = account;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "avatar_id")
    public NewImage getAvatar() {
        return avatar;
    }

    public void setAvatar(NewImage avatar) {
        this.avatar = avatar;
    }

    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "mobile_phone")
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "favourite_position", length = 20)
    public PositionsEnum getFavouritePosition() {
        return favouritePosition;
    }

    public void setFavouritePosition(PositionsEnum favouritePosition) {
        this.favouritePosition = favouritePosition;
    }

    @Size(min = 1, max = 200)
    @Column(name = "content")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "adrress_id", insertable = true, updatable = true, nullable = true)
    public NewAddress getAddress() {
        return newAddress;
    }

    public void setAddress(NewAddress newAddress) {
        this.newAddress = newAddress;
    }
}
