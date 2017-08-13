package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.dto.AddressDTO;
import com.soccer.ws.dto.ProfileDTO;
import com.soccer.ws.dto.RegistrationDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.*;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.utils.GeneralUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final String UPDATE_PASSWORD_SQL = "update account set password = ? where id = ?";
    private static final String GET_PASSWORD = "select password from account where id = ?";
    private final MessageSource messageSource;
    private final AccountDao accountDao;
    private final MailService mailService;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final DTOConversionHelper dtoConversionHelper;
    @Value("${base.url}")
    private String baseUrl;

    @Autowired
    public AccountServiceImpl(MessageSource messageSource, AccountDao accountDao, MailService mailService, JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, ImageService imageService, DTOConversionHelper dtoConversionHelper) {
        this.messageSource = messageSource;
        this.accountDao = accountDao;
        this.mailService = mailService;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.dtoConversionHelper = dtoConversionHelper;
    }

    @Override
    public AccountDTO register(RegistrationDTO registration) {
        Account toBeCreated = new Account.Builder()
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .username(registration.getEmail())
                .build();
        Account result = createAccountWithPassword(toBeCreated, registration.getPassword());
        mailService.sendPreConfiguredMail(messageSource.getMessage("mail.user.registered", new Object[]{baseUrl,
                result.getId(), registration.toString()}, Locale.ENGLISH));
        return new AccountDTO(result.getId(), result.getUsername(), result.getFirstName(), result.getLastName(), result.getRole().name(), result.isActive());
    }

    @Override
    @Transactional(readOnly = false)
    public void changeActivation(long id, boolean status) {
        Account account = accountDao.findOne(id);
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        account.setActive(status);
        accountDao.save(account);
    }

    @Override
    @Transactional(readOnly = false)
    public void elevate(long id) {
        Account account = accountDao.findOne(id);
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        account.setRole(Role.ADMIN);
        accountDao.save(account);
    }

    @Override
    @Transactional(readOnly = false)
    public void demote(long id) {
        Account account = accountDao.findOne(id);
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        account.setRole(Role.USER);
        accountDao.save(account);
    }

    @Override
    @Transactional(readOnly = false)
    public void changeRole(AccountDTO accountDTO, Role role) {
        Account account = accountDao.findOne(accountDTO.getId());
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        account.setRole(role);
        accountDao.save(account);
    }

    @Transactional(readOnly = false)
    private Account createAccountWithPassword(Account account, String password) {
        Account resultAccount = accountDao.save(account);
        //Update only if sign in provider is not specified
        if (account.getSignInProvider() == null) {
            String encPassword = passwordEncoder.encode(password);
            jdbcTemplate.update(UPDATE_PASSWORD_SQL, encPassword, account.getId());
        }
        return resultAccount;
    }

    @Transactional(readOnly = true)
    public boolean isValidUsername(String username) {
        return accountDao.findByUsername(username) == null;
    }

    public void validateUsernameExcludeCurrentId(String username, Long id, Errors errors) {
        if (accountDao.findByEmailExcludeCurrentId(username, id) != null) {
            errors.rejectValue("username", "error.duplicate.account.email",
                    new String[]{username}, null);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void setPasswordFor(long id, String password) {
        final Account account = accountDao.findOne(id);
        if (account == null) throw new ObjectNotFoundException("Account not found");
        String encPassword = passwordEncoder.encode(password);
        jdbcTemplate.update(UPDATE_PASSWORD_SQL, encPassword, id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkOldPassword(long id, String password) {
        final Account account = accountDao.findOne(id);
        if (account == null) throw new ObjectNotFoundException("Account cannot be found");
        String encodedPassword = getCurrentEncodedPasswordFor(account);
        return !(encodedPassword == null || encodedPassword.isEmpty()) && passwordEncoder.matches(password,
                encodedPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean passwordIsNullOrEmpty(Account account) {
        String encodedPassword = getCurrentEncodedPasswordFor(account);
        return encodedPassword == null || encodedPassword.isEmpty();
    }

    @Override
    @Transactional(readOnly = false)
    public Account update(ProfileDTO profileDTO) {
        Account account = accountDao.findOne(profileDTO.getId());
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        setAccountProfile(account, profileDTO);
        return accountDao.save(account);
    }

    @Override
    public List<Account> getAll() {
        List<Account> r = Lists.newArrayList(accountDao.findAll());
        Collections.sort(r);
        return r;
    }

    @Override
    public List<Account> getAllActivateAccounts() {
        List<Account> r = Lists.newArrayList(accountDao.findAllByActive(true));
        Collections.sort(r);
        return r;
    }

    @Override
    public Account getAccount(String id) {
        return accountDao.findOne(GeneralUtils.convertToLong(id));
    }

    @Override
    public Account getAccount(Long id) {
        return accountDao.findOne(id);
    }

    @Override
    public Account getActiveAccountByEmail(String email) {
        return accountDao.findByUsernameAndActiveStatus(email, true);
    }

    @Override
    public Account getActiveAccountById(String id) {
        return accountDao.findByIdAndActiveStatus(GeneralUtils.convertToLong(id), true);
    }

    @Override
    public List<Account> getAccountsByActivationStatus(boolean status) {
        return accountDao.findByActivationStatus(status);
    }

    @Override
    public List<Account> getAccountsWithActivationCode() {
        return accountDao.findByActivationCodeNotNull();
    }

    /**
     * private Account getUpdatedAccount(Account account, AccountProfileForm form) {
     * Account newAccount = accountDao.findOne(account.getId());
     * newAccount.setFirstName(form.getFirstName());
     * newAccount.setLastName(form.getLastName());
     * newAccount.setUsername(form.getUsername());
     * setAccountProfile(account, newAccount, form);
     * newAccount.getAccountSettings().setSendDoodleNotifications(form.isDoodleNotificationMails());
     * newAccount.getAccountSettings().setSendNewsNotifications(form.isNewsNotificationMails());
     * return newAccount;
     * }
     **/

    private String getCurrentEncodedPasswordFor(Account account) {
        return jdbcTemplate.queryForObject(GET_PASSWORD, String.class, account.getId());
    }

    private void setAccountProfile(Account account, ProfileDTO profileDTO) {
        AccountProfile accountProfile = account.getAccountProfile();
        //Set enum
        accountProfile.setFavouritePosition(profileDTO.getPosition());
        //Set mobile, phone, address, etc
        accountProfile.setPhone(profileDTO.getPhone());
        accountProfile.setMobilePhone(profileDTO.getMobilePhone());
        accountProfile.setAddress(createAddress(accountProfile.getAddress(), profileDTO.getAddress()));
        //Set image as a null value to remove, else set profile image with provided image.
        //updatedAccount.getAccountProfile().setAvatar(form.isRemoveAvatar() ?
        //null :
        //createProfileImage(updatedAccount.getAccountProfile().getAvatar(), form.getAvatar()));
    }

    private Address createAddress(Address address, AddressDTO addressDTO) {
        if (addressDTO == null) return null;
        if (address == null) address = new Address();
        address.setAddress(addressDTO.getAddress());
        address.setCity(addressDTO.getCity());
        address.setPostalCode(addressDTO.getPostalCode());
        return address;
    }
     

    private Image createProfileImage(Image image, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                return imageService.uploadProfileImage(file);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Could not upload image");
                //Fail if image cannot be uploaded
                throw new RuntimeException(e.getMessage(), e.getCause());
            }
        } else {
            return image;
        }
    }
}
