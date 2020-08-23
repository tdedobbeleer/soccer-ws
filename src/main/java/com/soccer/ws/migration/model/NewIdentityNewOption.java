package com.soccer.ws.migration.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * Created by u0090265 on 08/06/16.
 */
@Entity
@Table(name = "identity_option")
public class NewIdentityNewOption extends NewOption<UUID> {
    @Transient
    public Long oldId;

    public NewIdentityNewOption() {
        super();
    }

    public NewIdentityNewOption(UUID option) {
        this.setOption(option);
    }
}
