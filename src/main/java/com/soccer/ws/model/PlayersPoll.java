package com.soccer.ws.model;

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
public class PlayersPoll extends Poll<UUID> implements MultipleChoicePoll<UUID> {
    private Set<IdentityOption> options;
    private Set<MultipleChoicePlayerVote> votes;

    public PlayersPoll() {
        super();
    }

    @Override
    public void replaceVote(Vote vote) {
        boolean updated = false;
        for (final MultipleChoicePlayerVote element : getVotes()) {
            //If the vote already exists, replace answer
            if (element.getVoter().equals(vote.getVoter())) {
                element.setAnswer((UUID) vote.getAnswer());
                updated = true;
            }
        }
        if (!updated) {
            this.getVotes().add((MultipleChoicePlayerVote) vote);
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
    public Set<IdentityOption> getOptions() {
        return options;
    }

    public void setOptions(Set<IdentityOption> options) {
        this.options = options;
    }

    @Override
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true,
            mappedBy = "poll", targetEntity = Vote.class)
    public Set<MultipleChoicePlayerVote> getVotes() {
        return votes;
    }

    public void setVotes(Set<MultipleChoicePlayerVote> votes) {
        this.votes = votes;
    }


    @Override
    @Transient
    public RankingList<UUID> getResult() {
        List<Ranking<UUID>> rankings = Lists.newArrayList();
        int totalVotes = 0;
        for (Option<UUID> p : options) {
            Ranking r = new Ranking();
            r.setOption(p.getOption());
            for (Vote<UUID> v : votes) {
                if (v.getAnswer().equals(p.getOption())) {
                    r.setPoints(r.getPonts() + 1);
                    totalVotes++;
                }
            }
            rankings.add(r);
        }
        return new RankingList(rankings, totalVotes);
    }
}
