package com.soccer.ws.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by u0090265 on 08/06/16.
 */
@Entity
@Table(name = "identity_option")
public class IdentityOption extends Option<Long> {
    public IdentityOption() {
        super();
    }

    public IdentityOption(Long option) {
        this.setOption(option);
    }
}
