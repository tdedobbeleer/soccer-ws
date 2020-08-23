package com.soccer.ws.migration.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by u0090265 on 31/05/16.
 */
@MappedSuperclass
public abstract class NewOption<T> extends NewBaseClass {
    private T option;

    public NewOption() {
    }

    public NewOption(T option) {
        this.option = option;
    }

    @Column(name = "opt")
    public T getOption() {
        return option;
    }

    public void setOption(T option) {
        this.option = option;
    }
}
