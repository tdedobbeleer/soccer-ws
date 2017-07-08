package com.soccer.ws.service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.soccer.ws.model.Season;
import com.soccer.ws.persistence.SeasonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by u0090265 on 5/11/14.
 */

@Service
public class SeasonServiceImpl implements SeasonService {

    private final SeasonDao seasonDao;

    @Autowired
    public SeasonServiceImpl(SeasonDao seasonDao) {
        this.seasonDao = seasonDao;
    }

    @Override
    public List<Season> getSeasons() {
        return Lists.newArrayList(seasonDao.findAllOrderByDescriptionDesc());
    }

    @Override
    public Season getLatestSeason() {
        return Iterables.getFirst(seasonDao.findAllOrderByDescriptionDesc(), null);
    }

    @Override
    public Season create(String description) {
        final Season season = new Season();
        season.setDescription(description);
        return seasonDao.save(season);
    }
}
