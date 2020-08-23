package com.soccer.ws.migration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * User: Tom De Dobbeleer
 * Date: 1/17/14
 * Time: 9:43 PM
 * Remarks: none
 */

@Entity
@Table(name = "teams")
public class NewTeam extends NewBaseClass {
    private String name;
    private NewAddress newAddress;

    public NewTeam() {
    }

    @NotNull
    @Column(name = "description")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "adrress_id", insertable = true, updatable = true, nullable = false)
    public NewAddress getAddress() {
        return newAddress;
    }

    public void setAddress(NewAddress newAddress) {
        this.newAddress = newAddress;
    }
}
