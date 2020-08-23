package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewPoll;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 10/1/14.
 */
public interface NewPollDao extends PagingAndSortingRepository<NewPoll, Long>, JpaSpecificationExecutor<NewPoll> {

}
