package com.soccer.ws.selection.conditions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Doodle;
import com.soccer.ws.model.Presence;
import com.soccer.ws.model.Season;
import com.soccer.ws.service.CacheAdapter;
import com.soccer.ws.service.SeasonService;
import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PresenceCondition extends AbstractCondition {
    private int years;
    private CacheAdapter cacheAdapter;
    private SeasonService seasonService;

    public PresenceCondition(int years, float weight, CacheAdapter cacheAdapter, SeasonService seasonService) {
        super(weight);
        this.seasonService = seasonService;
        this.cacheAdapter = cacheAdapter;
        this.years = years;
    }

    @Override
    public Mono<Map<Account, Float>> calculate(Doodle doodle) {
        return Flux.fromIterable(seasonService.getSeasons())
                .sort(compare())
                .limitRequest(years)
                //Get all stats
                .flatMap(s -> stats(s, doodle.getPresences()))
                //sum of all stats
                .reduce(Maps.newConcurrentMap(), (m1, m2) -> {
                    Map<Account, Float> m = m2.entrySet().stream().map(e -> {
                        Float sum = 0f;
                        for (Float f : e.getValue()) {
                            sum = sum + f;
                        }
                        return ImmutableMap.of(e.getKey(), (sum / years) * weight);
                    }).findFirst().orElse(ImmutableMap.of());
                    m1.putAll(m);
                    return m1;
                });
    }

    private Mono<Map<Account, List<Float>>> stats(Season s, Set<Presence> presences) {
        int total = cacheAdapter.getMatchesForSeason(s.getId(), false).size();

        //Get the stats, put them in a Pair
        //Then put all pairs together in one big map (Account - List of floats
        return Flux.fromIterable(cacheAdapter.getStatisticsForSeason(s.getId(), false))
                .map(statistic -> Pair.of(
                        getAccountFromPresences(presences, statistic.getAccount().getId()),
                        safeDivide(total, statistic.getPlayed())))
                .reduce(Maps.newHashMap(), (x1, x2) -> {
                    if (x1.containsKey(x2.getKey())) {
                        x1.get(x2.getKey()).add(x2.getValue());
                    } else {
                        x1.put(x2.getKey(), Lists.newArrayList(x2.getValue()));
                    }
                    return x1;
                });
    }

    private Comparator<Season> compare() {
        return (s1, s2) -> {
            if (s1.getCreated().isBefore(s2.getCreated())) {
                return -1;
            } else if (s1.getCreated().isAfter(s2.getCreated())) {
                return 1;
            } else {
                return 0;
            }
        };
    }

    private float safeDivide(int v, int d) {
        if (v != 0 && d != 0) {
            return v / d;
        }
        return 0;
    }

    private Account getAccountFromPresences(Set<Presence> presences, Long id) {
        return presences.stream().filter(p -> p.getAccount().getId().equals(id)).map(Presence::getAccount).findFirst().orElseThrow(() -> new ObjectNotFoundException("Account could not be found"));
    }


}
