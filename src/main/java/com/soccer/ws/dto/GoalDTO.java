package com.soccer.ws.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Created by u0090265 on 15/07/16.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GoalDTO {
    private Integer order;
    private AccountDTO scorer;
    private AccountDTO assist;

    public GoalDTO(AccountDTO scorer, AccountDTO assist, Integer order) {
        this.scorer = scorer;
        this.assist = assist;
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoalDTO goalDTO = (GoalDTO) o;

        if (!Objects.equals(order, goalDTO.order)) return false;
        if (!Objects.equals(scorer, goalDTO.scorer)) return false;
        return Objects.equals(assist, goalDTO.assist);
    }

    @Override
    public int hashCode() {
        int result = order != null ? order.hashCode() : 0;
        result = 31 * result + (scorer != null ? scorer.hashCode() : 0);
        result = 31 * result + (assist != null ? assist.hashCode() : 0);
        return result;
    }
}
