package com.soccer.ws.dto;

import lombok.*;

/**
 * Created by u0090265 on 10/10/15.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AccountStatisticDTO {
    private AccountDTO account;
    private int goals;
    private int assists;
    private int played;
    private int motm;

    public AccountStatisticDTO(AccountDTO account, int goals, int assists, int played, int motm) {
        this.account = account;
        this.goals = goals;
        this.assists = assists;
        this.played = played;
        this.motm = motm;
    }

    public AccountStatisticDTO(AccountDTO account) {
        this.account = account;
    }

}
