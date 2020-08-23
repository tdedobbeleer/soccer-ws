package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewTeam;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 5/10/14.
 */
public interface NewTeamDao extends PagingAndSortingRepository<NewTeam, Long>, JpaSpecificationExecutor<NewTeam> {

}
