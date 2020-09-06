package com.soccer.ws.persistence;

import com.soccer.ws.model.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface AccountDao extends PagingAndSortingRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    Account findByUsernameIgnoreCase(String email);

    Account findByUsernameIgnoreCaseAndActive(String username, boolean active);

    Account findByIdAndActive(UUID id, boolean active);

    Account findByUsernameIgnoreCaseAndIdNot(String email, UUID id);

    List<Account> findAllByPwdRecoveryNotNull();

    List<Account> findAllByActive(boolean active);
}
