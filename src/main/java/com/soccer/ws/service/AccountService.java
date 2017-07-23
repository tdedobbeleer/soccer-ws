package com.soccer.ws.service;

import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.dto.RegistrationDTO;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Role;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.List;

public interface AccountService {
    AccountDTO register(RegistrationDTO registration);

    //Account updateAccount(Account account, AccountProfileForm form);

    void changeActivation(long id, boolean status);

    @Transactional(readOnly = false)
    void changeRole(AccountDTO accountDTO, Role role);

    @Transactional
    Account saveAccount(Account account);


    //@Transactional
    //Account activateAccount(ActivateAccountForm form, Locale locale, Errors errors);

    boolean isValidUsername(String email);

    void validateUsernameExcludeCurrentId(String email, Long id, Errors errors);

    @Transactional(readOnly = false)
    void setPasswordFor(Account account, String password);

    @Transactional(readOnly = false)
    boolean checkOldPassword(Account account, String password);

    @Transactional(readOnly = true)
    boolean passwordIsNullOrEmpty(Account account);

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

