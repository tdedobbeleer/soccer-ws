package com.soccer.ws.migration.model;

import com.google.common.collect.Sets;
import com.soccer.ws.model.DoodleStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by u0090265 on 10/1/14.
 */
@Entity
@Table(name = "doodle")
public class NewDoodle extends NewBaseClass {

    private Set<NewPresence> newPresences;
    private DoodleStatusEnum status = DoodleStatusEnum.CLOSED;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "doodle_id")
    public Set<NewPresence> getPresences() {
        if (newPresences == null) newPresences = Sets.newHashSet();
        return newPresences;
    }

    public void setPresences(Set<NewPresence> newPresences) {
        this.newPresences = newPresences;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @NotNull
    public DoodleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DoodleStatusEnum status) {
        this.status = status;
    }

    @Transient
    public NewPresence.PresenceType getPresenceType(final NewAccount account) {
        if (account == null) return NewPresence.PresenceType.ANONYMOUS;
        for (NewPresence p : getPresences()) {
            if (p.getAccount().equals(account)) {
                return p.isPresent() ?
                        p.isReserve() ? NewPresence.PresenceType.RESERVE : NewPresence.PresenceType.PRESENT :
                        NewPresence.PresenceType.NOT_PRESENT;
            }
        }
        return NewPresence.PresenceType.NOT_FILLED_IN;
    }

    @Transient
    public int countPresences() {
        int i = 0;
        for (NewPresence p : getPresences()) {
            if (p.isPresent() && !p.isReserve()) {
                i++;
            }
        }
        return i;
    }

    public NewPresence getPresenceFor(NewAccount account) {
        if (newPresences == null) return null;
        for (NewPresence p : getPresences()) {
            if (p.getAccount().equals(account)) {
                return p;
            }
        }
        return null;
    }
}
