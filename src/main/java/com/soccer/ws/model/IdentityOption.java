package com.soccer.ws.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by u0090265 on 08/06/16.
 */
@Entity
@Table(name = "identity_option")
public class IdentityOption extends Option<UUID> {
    public IdentityOption() {
        super();
    }

    public IdentityOption(UUID option) {
        this.setOption(option);
    }
}
