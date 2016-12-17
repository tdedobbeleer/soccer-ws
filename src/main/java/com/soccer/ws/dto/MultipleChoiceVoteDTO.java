package com.soccer.ws.dto;

/**
 * Created by u0090265 on 11/06/16.
 */
public class MultipleChoiceVoteDTO<T> {
    private T answer;
    private AccountDTO account;

    public MultipleChoiceVoteDTO(T answer) {
        this.answer = answer;
    }

    public MultipleChoiceVoteDTO() {
    }

    public T getAnswer() {
        return answer;
    }

    public void setAnswer(T answer) {
        this.answer = answer;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "MultipleChoiceVoteDTO{" +
                "answer=" + answer +
                ", account=" + account +
                '}';
    }
}
