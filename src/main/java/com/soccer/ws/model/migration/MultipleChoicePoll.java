package com.soccer.ws.model.migration;

import com.soccer.ws.model.RankingList;

/**
 * Created by u0090265 on 18/06/16.
 */
public interface MultipleChoicePoll<T> {
    RankingList<T> getResult();
}