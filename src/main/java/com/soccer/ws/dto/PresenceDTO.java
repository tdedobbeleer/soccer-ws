package com.soccer.ws.dto;

import com.soccer.ws.model.Presence;
import lombok.*;

/**
 * Created by u0090265 on 09/09/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PresenceDTO implements Comparable<PresenceDTO> {
    private AccountDTO account;
    private boolean isEditable;
    private Presence.PresenceType type;
    private String modified;

    public PresenceDTO(AccountDTO account, Presence.PresenceType type, String modified, boolean isEditable) {
        this.account = account;
        this.type = type;
        this.isEditable = isEditable;
        this.modified = modified;
    }

    @Override
    public int compareTo(PresenceDTO o) {
        return account.compareTo(o.account);
    }
}
