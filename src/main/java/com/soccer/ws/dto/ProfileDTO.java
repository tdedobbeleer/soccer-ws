package com.soccer.ws.dto;

import com.soccer.ws.data.PositionsEnum;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by u0090265 on 10.07.17.
 */
public class ProfileDTO extends BaseClassDTO {
    private String phone;
    private String mobilePhone;
    private MultipartFile avatar;
    private PositionsEnum position;
    private boolean doodleNotificationMails;
    private boolean newsNotificationMails;
    private String avatarUrl;
    private String address;
    private String postalCode;
    private String city;

    public boolean isDoodleNotificationMails() {
        return doodleNotificationMails;
    }

    public void setDoodleNotificationMails(boolean doodleNotificationMails) {
        this.doodleNotificationMails = doodleNotificationMails;
    }

    public boolean isNewsNotificationMails() {
        return newsNotificationMails;
    }

    public void setNewsNotificationMails(boolean newsNotificationMails) {
        this.newsNotificationMails = newsNotificationMails;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public PositionsEnum getPosition() {
        return position;
    }

    public void setPosition(PositionsEnum position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = WordUtils.capitalize(address);
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = WordUtils.capitalize(city);
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                ", phone='" + phone + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", avatar=" + avatar +
                ", position=" + position +
                ", doodleNotificationMails=" + doodleNotificationMails +
                ", newsNotificationMails=" + newsNotificationMails +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                "} " + super.toString();
    }
}
