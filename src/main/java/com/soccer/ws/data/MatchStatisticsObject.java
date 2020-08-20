package com.soccer.ws.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Created by u0090265 on 6/27/15.
 */
@AllArgsConstructor
@Getter
@Setter
public class MatchStatisticsObject {
    private Long id;
    private Long number;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchStatisticsObject that = (MatchStatisticsObject) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(number, that.number)) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (number ^ (number >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatisticsGoalsObject{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
