package com.soccer.ws.persistence;

import com.soccer.ws.model.Doodle;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 10/1/14.
 */
public interface DoodleDao extends PagingAndSortingRepository<Doodle, Long>, JpaSpecificationExecutor<Doodle> {
}
