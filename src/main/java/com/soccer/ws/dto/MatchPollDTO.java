package com.soccer.ws.dto;

import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by u0090265 on 10/06/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class MatchPollDTO extends BaseClassDTO {
    private List<VotesDTO> votes;
    private List<AccountDTO> options;
    private int totalVotes;
    private String status;
    private String matchDescription;
    private String matchDate;
    private UUID matchId;

    public MatchPollDTO(UUID id, UUID matchId, List<VotesDTO> votes, List<AccountDTO> accounts, int totalVotes,
                        String status, String matchDescription, String matchDate) {
        this.votes = votes;
        this.totalVotes = totalVotes;
        this.status = status;
        this.setId(id);
        this.setOptions(accounts);
        this.matchDate = matchDate;
        this.matchDescription = matchDescription;
        this.matchId = matchId;
    }

    public void setOptions(List<AccountDTO> options) {
        if (options != null) {
            Collections.sort(options);
        }
        this.options = options;
    }
}
