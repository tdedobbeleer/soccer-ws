package com.soccer.ws.migration.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by u0090265 on 11/06/16.
 */
public class NewRankingList<T> {
    List<NewRanking<T>> newRankings;
    int totalVotes;

    public NewRankingList(List<NewRanking<T>> newRankings, int totalVotes) {
        this.newRankings = newRankings;
        this.totalVotes = totalVotes;
    }

    public List<NewRanking<T>> getRankings() {
        Collections.sort(newRankings, Collections.reverseOrder());
        return newRankings;
    }

    public void setRankings(List<NewRanking<T>> newRankings) {
        this.newRankings = newRankings;
    }

    public Optional<NewRanking<T>> getHighestRanked() {
        if (newRankings != null) {
            List<NewRanking<T>> r = newRankings.stream().collect(groupingBy(NewRanking::getPonts, TreeMap::new, toList()))
                    .lastEntry()
                    .getValue();
            if (r.size() == 1) {
                return r.stream().findFirst();
            }
        }
        return Optional.empty();
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }
}
