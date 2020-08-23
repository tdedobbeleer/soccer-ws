package com.soccer.ws.migration.model;

import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by u0090265 on 29/05/16.
 * Class for poll which has players (id's) as options. Not using a generic String makes it a bit less generic but
 * more type safe.
 */
@Entity
@Table(name = "players_poll")
@PrimaryKeyJoinColumn(name = "poll_id", referencedColumnName = "id")
public class NewPlayersNewPoll extends NewPoll<UUID> implements NewMultipleChoicePoll<UUID> {
    private Set<NewIdentityNewOption> options;
    private Set<NewMultipleChoicePlayerNewVote> votes;

    public NewPlayersNewPoll() {
        super();
    }

    @Override
    public void replaceVote(NewVote newVote) {
        boolean updated = false;
        for (final NewMultipleChoicePlayerNewVote element : getVotes()) {
            //If the vote already exists, replace answer
            if (element.getVoter().equals(newVote.getVoter())) {
                element.setAnswer((UUID) newVote.getAnswer());
                updated = true;
            }
        }
        if (!updated) {
            this.getVotes().add((NewMultipleChoicePlayerNewVote) newVote);
        }
    }

    @Override
    @CollectionTable(
            name = "poll_options",
            joinColumns = {@JoinColumn(
                    name = "id"
            )}
    )
    @OrderColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<NewIdentityNewOption> getOptions() {
        return options;
    }

    public void setOptions(Set<NewIdentityNewOption> options) {
        this.options = options;
    }

    @Override
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true,
            mappedBy = "poll", targetEntity = NewVote.class)
    public Set<NewMultipleChoicePlayerNewVote> getVotes() {
        return votes;
    }

    public void setVotes(Set<NewMultipleChoicePlayerNewVote> votes) {
        this.votes = votes;
    }


    @Override
    @Transient
    public NewRankingList<UUID> getResult() {
        List<NewRanking<UUID>> newRankings = Lists.newArrayList();
        int totalVotes = 0;
        for (NewOption<UUID> p : options) {
            NewRanking r = new NewRanking();
            r.setOption(p.getOption());
            for (NewVote<UUID> v : votes) {
                if (v.getAnswer().equals(p.getOption())) {
                    r.setPoints(r.getPonts() + 1);
                    totalVotes++;
                }
            }
            newRankings.add(r);
        }
        return new NewRankingList(newRankings, totalVotes);
    }
}
