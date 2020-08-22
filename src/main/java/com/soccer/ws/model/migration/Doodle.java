package com.soccer.ws.model.migration;

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

    private Set<com.soccer.ws.model.migration.Presence> presences;
    private com.soccer.ws.model.migration.DoodleStatusEnum status = com.soccer.ws.model.migration.DoodleStatusEnum.CLOSED;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "doodle_id")
    public Set<com.soccer.ws.model.migration.Presence> getPresences() {
        if (presences == null) presences = Sets.newHashSet();
        return presences;
    }

    public void setPresences(Set<com.soccer.ws.model.migration.Presence> presences) {
        this.presences = presences;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @NotNull
    public com.soccer.ws.model.migration.DoodleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DoodleStatusEnum status) {
        this.status = status;
    }

    @Transient
    public com.soccer.ws.model.migration.Presence.PresenceType getPresenceType(final com.soccer.ws.model.migration.Account account) {
        if (account == null) return com.soccer.ws.model.migration.Presence.PresenceType.ANONYMOUS;
        for (com.soccer.ws.model.migration.Presence p : getPresences()) {
            if (p.getAccount().equals(account)) {
                return p.isPresent() ?
                        p.isReserve() ? com.soccer.ws.model.migration.Presence.PresenceType.RESERVE : com.soccer.ws.model.migration.Presence.PresenceType.PRESENT :
                        com.soccer.ws.model.migration.Presence.PresenceType.NOT_PRESENT;
            }
        }
        return com.soccer.ws.model.migration.Presence.PresenceType.NOT_FILLED_IN;
    }

    @Transient
    public int countPresences() {
        int i = 0;
        for (com.soccer.ws.model.migration.Presence p : getPresences()) {
            if (p.isPresent() && !p.isReserve()) {
                i++;
            }
        }
        return i;
    }

    public com.soccer.ws.model.migration.Presence getPresenceFor(Account account) {
        if (presences == null) return null;
        for (Presence p : getPresences()) {
            if (p.getAccount().equals(account)) {
                return p;
            }
        }
        return null;
    }
}
