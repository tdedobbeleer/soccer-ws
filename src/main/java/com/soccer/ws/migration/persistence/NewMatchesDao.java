package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewMatch;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 5/3/14.
 */
public interface NewMatchesDao extends PagingAndSortingRepository<NewMatch, Long>, JpaSpecificationExecutor<NewMatch> {

}
