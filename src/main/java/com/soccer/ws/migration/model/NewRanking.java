package com.soccer.ws.migration.model;

/**
 * Created by u0090265 on 04/06/16.
 */
public class NewRanking<T> implements Comparable<NewRanking<T>> {
    private int points;
    private T option;

    public NewRanking(int points, T option) {
        this.points = points;
        this.option = option;
    }

    public NewRanking() {
    }

    public T getOption() {
        return option;
    }

    public void setOption(T option) {
        this.option = option;
    }

    public int getPonts() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int compareTo(NewRanking<T> newRanking) {
        return points > newRanking.points ? 1 : points < newRanking.points ? -1 : 0;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "points=" + points +
                ", option=" + option +
                '}';
    }
}
