package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NewAccountDao extends PagingAndSortingRepository<NewAccount, Long>, JpaSpecificationExecutor<NewAccount> {

}
