package com.soccer.ws.migration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by u0090265 on 08/06/16.
 */
@Entity
@Table(name = "multiple_choice_player_vote")
@PrimaryKeyJoinColumn(name = "vote_id", referencedColumnName = "id")
public class NewMultipleChoicePlayerNewVote extends NewVote<UUID> {
    public NewMultipleChoicePlayerNewVote() {
        super();
    }

    public NewMultipleChoicePlayerNewVote(NewAccount voter, UUID answer) {
        super();
        this.setVoter(voter);
        this.setAnswer(answer);
    }

    @NotNull
    @Column(name = "answer")
    @Override
    public UUID getAnswer() {
        return this.answer;
    }
}
