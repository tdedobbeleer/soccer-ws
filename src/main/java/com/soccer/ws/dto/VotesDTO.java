package com.soccer.ws.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by u0090265 on 10/06/16.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class VotesDTO implements Comparable<VotesDTO> {
    private AccountDTO account;
    private int votes;

    public VotesDTO(AccountDTO accountDTO, int votes) {
        this.account = accountDTO;
        this.votes = votes;
    }

    @Override
    public int compareTo(VotesDTO o) {
        if (this.votes < o.votes) return 1;
        else if (this.votes == o.votes) return this.account.compareTo(o.account);
        else return -1;
    }
}
