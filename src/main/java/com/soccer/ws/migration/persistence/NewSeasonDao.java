package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewSeason;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 5/3/14.
 */
public interface NewSeasonDao extends PagingAndSortingRepository<NewSeason, Long>, JpaSpecificationExecutor<NewSeason> {

}
