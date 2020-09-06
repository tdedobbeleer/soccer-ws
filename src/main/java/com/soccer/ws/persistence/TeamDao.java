package com.soccer.ws.persistence;

import com.soccer.ws.model.Team;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 5/10/14.
 */
public interface TeamDao extends PagingAndSortingRepository<Team, java.util.UUID>, JpaSpecificationExecutor<Team> {
    Team findOneByName(String name);
}
