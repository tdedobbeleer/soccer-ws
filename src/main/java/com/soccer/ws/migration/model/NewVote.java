package com.soccer.ws.migration.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by u0090265 on 29/05/16.
 */
@Entity
@Table(name = "vote")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class NewVote<T> extends NewBaseClass {
    protected T answer;
    private NewAccount voter;
    private NewPoll newPoll;

    public NewVote() {
    }

    @Transient
    public abstract T getAnswer();

    public void setAnswer(T answer) {
        this.answer = answer;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voter", nullable = false)
    public NewAccount getVoter() {
        return voter;
    }

    public void setVoter(NewAccount voter) {
        this.voter = voter;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poll_id")
    @NotNull
    public NewPoll getPoll() {
        return newPoll;
    }

    public void setPoll(NewPoll newPoll) {
        this.newPoll = newPoll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewVote<?> newVote = (NewVote<?>) o;

        if (voter != null ? !voter.equals(newVote.voter) : newVote.voter != null) return false;
        return newPoll != null ? newPoll.equals(newVote.newPoll) : newVote.newPoll == null;

    }

    @Override
    public int hashCode() {
        int result = voter != null ? voter.hashCode() : 0;
        result = 31 * result + (newPoll != null ? newPoll.hashCode() : 0);
        return result;
    }
}
