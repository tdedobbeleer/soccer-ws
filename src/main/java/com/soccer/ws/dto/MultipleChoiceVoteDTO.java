package com.soccer.ws.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by u0090265 on 11/06/16.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MultipleChoiceVoteDTO<T> {
    private T answer;
    private AccountDTO account;

    public MultipleChoiceVoteDTO(T answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "MultipleChoiceVoteDTO{" +
                "answer=" + answer +
                ", account=" + account +
                '}';
    }
}
