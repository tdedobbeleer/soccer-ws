package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewDoodle;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 10/1/14.
 */
public interface NewDoodleDao extends PagingAndSortingRepository<NewDoodle, Long>, JpaSpecificationExecutor<NewDoodle> {
}
