package com.soccer.ws.model.migration;

import javax.persistence.*;

/**
 * Created by u0090265 on 10/1/14.
 */
@Entity
@Table(name = "presences", uniqueConstraints = @UniqueConstraint(columnNames = {"doodle_id", "account_id"}))
public class Presence extends BaseClass {
    private com.soccer.ws.model.migration.Account account;
    private boolean present;
    private boolean reserve;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    public com.soccer.ws.model.migration.Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Column(name = "present")
    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Column(name = "reserve")
    public boolean isReserve() {
        return reserve;
    }

    public void setReserve(boolean reserve) {
        this.reserve = reserve;
    }

    public enum PresenceType {
        NOT_PRESENT, PRESENT, NOT_FILLED_IN, ANONYMOUS, RESERVE
    }
}
