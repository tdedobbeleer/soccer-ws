package com.soccer.ws.model;

/**
 * Created by u0090265 on 04/06/16.
 */
public class Ranking<T> implements Comparable<Ranking<T>> {
    private int points;
    private T option;

    public Ranking(int points, T option) {
        this.points = points;
        this.option = option;
    }

    public Ranking() {
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
    public int compareTo(Ranking<T> ranking) {
        return points > ranking.points ? 1 : points < ranking.points ? -1 : 0;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "points=" + points +
                ", option=" + option +
                '}';
    }
}
