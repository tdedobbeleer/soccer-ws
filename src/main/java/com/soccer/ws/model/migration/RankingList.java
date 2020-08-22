package com.soccer.ws.model.migration;

import com.soccer.ws.model.Ranking;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by u0090265 on 11/06/16.
 */
public class RankingList<T> {
    List<Ranking<T>> rankings;
    int totalVotes;

    public RankingList(List<Ranking<T>> rankings, int totalVotes) {
        this.rankings = rankings;
        this.totalVotes = totalVotes;
    }

    public List<Ranking<T>> getRankings() {
        Collections.sort(rankings, Collections.reverseOrder());
        return rankings;
    }

    public void setRankings(List<Ranking<T>> rankings) {
        this.rankings = rankings;
    }

    public Optional<Ranking<T>> getHighestRanked() {
        if (rankings != null) {
            List<Ranking<T>> r = rankings.stream().collect(groupingBy(Ranking::getPonts, TreeMap::new, toList()))
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
