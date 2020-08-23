package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewAddress;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 9/11/14.
 */
public interface NewAddressDao extends PagingAndSortingRepository<NewAddress, Long>, JpaSpecificationExecutor<NewAddress> {
}
