package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewNews;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * User: Tom De Dobbeleer
 * Date: 12/20/13
 * Time: 9:14 AM
 * Remarks: none
 */
public interface NewNewsDao extends PagingAndSortingRepository<NewNews, Long>, JpaSpecificationExecutor<NewNews> {

}
