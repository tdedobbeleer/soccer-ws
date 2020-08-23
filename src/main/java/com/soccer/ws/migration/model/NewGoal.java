package com.soccer.ws.migration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by u0090265 on 5/3/14.
 */

@Entity
@Table(name = "goals")
public class NewGoal extends NewBaseClass implements Comparable<NewGoal> {
    private int order;
    private NewAccount scorer;
    private NewAccount assist;
    private NewMatch newMatch;

    public NewGoal() {
    }

    @Column(name = "goal_order")
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "scorer", nullable = true)
    public NewAccount getScorer() {
        return scorer;
    }

    public void setScorer(NewAccount scorer) {
        this.scorer = scorer;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assist", nullable = true)
    public NewAccount getAssist() {
        return assist;
    }

    public void setAssist(NewAccount assist) {
        this.assist = assist;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "match_id", referencedColumnName = "id")
    public NewMatch getMatch() {
        return newMatch;
    }

    public void setMatch(NewMatch newMatch) {
        this.newMatch = newMatch;
    }

    @Override
    public int compareTo(NewGoal o) {
        return Integer.compare(order, o.order);
    }
}
