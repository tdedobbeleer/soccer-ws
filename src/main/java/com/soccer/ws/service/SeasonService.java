package com.soccer.ws.service;

import com.soccer.ws.model.Season;

import java.util.List;

/**
 * Created by u0090265 on 5/11/14.
 */
public interface SeasonService {
    List<Season> getSeasons();

    Season getLatestSeason();
}
