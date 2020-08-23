package com.soccer.ws.migration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * User: Tom De Dobbeleer
 * Date: 1/17/14
 * Time: 9:37 PM
 * Remarks: none
 */

@Entity
@Table(name = "season")
public class NewSeason extends NewBaseClass {
    private String description;

    public NewSeason() {
    }

    @NotNull
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
