package com.soccer.ws.model;

import com.google.common.collect.Sets;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by u0090265 on 10/1/14.
 */
@Entity
@Table(name = "doodle")
public class Doodle extends BaseClass {

    private Set<Presence> presences;
    private DoodleStatusEnum status = DoodleStatusEnum.CLOSED;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "doodle_id")
    public Set<Presence> getPresences() {
        if (presences == null) presences = Sets.newHashSet();
        return presences;
    }

    public void setPresences(Set<Presence> presences) {
        this.presences = presences;
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
    public Presence.PresenceType getPresenceType(final Account account) {
        if (account == null) return Presence.PresenceType.ANONYMOUS;
        for (Presence p : getPresences()) {
            if (p.getAccount().equals(account)) {
                return p.isPresent() ?
                        p.isReserve() ? Presence.PresenceType.RESERVE : Presence.PresenceType.PRESENT :
                        Presence.PresenceType.NOT_PRESENT;
            }
        }
        return Presence.PresenceType.NOT_FILLED_IN;
    }

    @Transient
    public int countPresences() {
        int i = 0;
        for (Presence p : getPresences()) {
            if (p.isPresent() && !p.isReserve()) {
                i++;
            }
        }
        return i;
    }

    public Presence getPresenceFor(Account account) {
        if (presences == null) return null;
        for (Presence p : getPresences()) {
            if (p.getAccount().equals(account)) {
                return p;
            }
        }
        return null;
    }
}
