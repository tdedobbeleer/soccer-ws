package com.soccer.ws.persistence;

import com.soccer.ws.model.Address;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 9/11/14.
 */
public interface AddressDao extends PagingAndSortingRepository<Address, java.util.UUID>, JpaSpecificationExecutor<Address> {
}
