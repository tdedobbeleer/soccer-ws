package com.soccer.ws.dto;

import com.soccer.ws.model.BaseClass;
import com.soccer.ws.model.DoodleStatusEnum;
import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by u0090265 on 09/09/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DoodleDTO extends BaseClass {
    @Setter(AccessLevel.NONE)
    private List<PresenceDTO> presences;
    @Setter(AccessLevel.NONE)
    private List<PresenceDTO> reserves;
    private PresenceDTO currentPresence;
    private int total;
    private DoodleStatusEnum status;

    public DoodleDTO(long id, List<PresenceDTO> presences, List<PresenceDTO> reserves, PresenceDTO currentPresence, int total, DoodleStatusEnum status) {
        this.id = id;
        this.total = total;
        this.status = status;
        this.currentPresence = currentPresence;
        setPresences(presences);
        setReserves(reserves);
    }

    public void setPresences(List<PresenceDTO> presences) {
        if (presences != null) {
            Collections.sort(presences);
        }
        this.presences = presences;
    }

    public void setReserves(List<PresenceDTO> reserves) {
        if (reserves != null) {
            Collections.sort(reserves);
        }
        this.reserves = reserves;
    }
}
