package com.soccer.ws.service;

import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.dto.ProfileDTO;
import com.soccer.ws.dto.RegistrationDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountService {
    AccountDTO register(RegistrationDTO registration);

    //Account updateAccount(Account account, AccountProfileForm form);

    void changeActivation(long id, boolean status);

    @Transactional(readOnly = false)
    void elevate(long id);

    @Transactional(readOnly = false)
    void demote(long id);

    @Transactional(readOnly = false)
    void changeRole(AccountDTO accountDTO, Role role);


    //@Transactional
    //Account activateAccount(ActivateAccountForm form, Locale locale, Errors errors);

    boolean isValidUsername(String email);

    boolean isValidUsernameExcludeCurrentId(String username, Long id);

    @Transactional(readOnly = false)
    void setPasswordFor(long id, String password);

    @Transactional(readOnly = false)
    boolean checkOldPassword(long id, String password);

    @Transactional(readOnly = true)
    boolean passwordIsNullOrEmpty(Account account);

    @Transactional(readOnly = false)
    Account update(ProfileDTO profileDTO);

    @Transactional(readOnly = true)
    List<Account> getAll();

    @Transactional(readOnly = true)
    List<Account> getAllActivateAccounts();

    @Transactional(readOnly = true)
    Account getAccount(String id);

    Account getAccount(Long id);

    Account getActiveAccountByEmail(String email);

    Account getActiveAccountById(String id);

    List<Account> getAccountsByActivationStatus(boolean status);

    List<Account> getAccountsWithActivationCode();
}

