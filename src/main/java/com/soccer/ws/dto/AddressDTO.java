package com.soccer.ws.dto;

/**
 * Created by u0090265 on 17.02.17.
 */
public class AddressDTO extends BaseClassDTO {
    private Integer postalCode;
    private String address;
    private String city;
    private String googleLink;

    public AddressDTO(Long id, Integer postalCode, String address, String city, String googleLink) {
        super(id);
        this.postalCode = postalCode;
        this.address = address;
        this.city = city;
        this.googleLink = googleLink;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGoogleLink() {
        return googleLink;
    }

    public void setGoogleLink(String googleLink) {
        this.googleLink = googleLink;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "postalCode=" + postalCode +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", googleLink='" + googleLink + '\'' +
                '}';
    }
}
