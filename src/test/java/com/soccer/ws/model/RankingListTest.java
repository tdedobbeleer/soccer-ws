package com.soccer.ws.model;

import org.assertj.core.util.Lists;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.*;

public class RankingListTest {

    @Test
    public void testGetHighestRankedEqual() {
        Ranking<String> r1 = new Ranking<>(1, "r1");
        Ranking<String> r2 = new Ranking<>(2, "r2");
        Ranking<String> r3 = new Ranking<>(3, "r3");
        Ranking<String> r4 = new Ranking<>(4, "r4");
        Ranking<String> r5 = new Ranking<>(5, "r5");
        Ranking<String> r6 = new Ranking<>(6, "r6");
        Ranking<String> r7 = new Ranking<>(6, "r7");

        RankingList<String> l = new RankingList<>(Lists.newArrayList(r1, r2, r3, r4, r5, r6, r7), 7);

        Optional<Ranking<String>> r = l.getHighestRanked();

        assertFalse(r.isPresent());
    }

    @Test
    public void testGetHighestRanked() {
        Ranking<String> r1 = new Ranking<>(1, "r1");
        Ranking<String> r2 = new Ranking<>(2, "r2");
        Ranking<String> r3 = new Ranking<>(3, "r3");
        Ranking<String> r4 = new Ranking<>(4, "r4");
        Ranking<String> r5 = new Ranking<>(5, "r5");
        Ranking<String> r6 = new Ranking<>(6, "r6");
        Ranking<String> r7 = new Ranking<>(7, "r7");

        RankingList<String> l = new RankingList<>(Lists.newArrayList(r1, r2, r3, r4, r5, r6, r7), 7);

        Optional<Ranking<String>> r = l.getHighestRanked();

        assertTrue(r.isPresent());
        assertEquals(r7, r.get());
    }
}
