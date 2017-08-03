package com.soccer.ws.dto;

/**
 * Created by u0090265 on 10/2/15.
 */
public class TeamDTO extends BaseClassDTO {
    private String name;
    private AddressDTO address;

    public TeamDTO() {}

    public TeamDTO(Long id, String name, AddressDTO address) {
        super(id);
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
