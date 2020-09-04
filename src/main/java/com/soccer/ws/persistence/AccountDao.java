package com.soccer.ws.persistence;

import com.soccer.ws.model.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface AccountDao extends PagingAndSortingRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    @Query("select a from Account a where a.username = ?1")
    Account findByUsername(String email);

    @Query("select a from Account a where a.username = ?1 AND a.active = ?2")
    Account findByUsernameAndActiveStatus(String username, boolean active);

    @Query("select a from Account a where a.id = ?1 AND a.active = ?2")
    Account findByIdAndActiveStatus(UUID id, boolean active);

    @Query("select a from Account a where a.username = ?1 AND a.id <> ?2")
    Account findByEmailExcludeCurrentId(String email, UUID id);

    @Query("select a from Account a where a.active = ?1")
    List<Account> findByActivationStatus(boolean status);

    @Query("select a from Account a where a.pwdRecovery IS NOT NULL")
    List<Account> findByActivationCodeNotNull();

    List<Account> findAllByActive(boolean active);
}
