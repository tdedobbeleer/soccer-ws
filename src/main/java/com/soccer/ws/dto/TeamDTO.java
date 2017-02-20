package com.soccer.ws.dto;

import com.soccer.ws.model.BaseClass;

/**
 * Created by u0090265 on 10/2/15.
 */
public class TeamDTO extends BaseClass {
    private String name;
    private AddressDTO address;

    public TeamDTO(String name, AddressDTO address) {
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
