package com.soccer.ws.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.dto.AddressDTO;
import com.soccer.ws.dto.ProfileDTO;
import com.soccer.ws.dto.RegistrationDTO;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.*;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.utils.Constants;
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
    @Transactional(readOnly = false)
    public AccountDTO register(RegistrationDTO registration) {
        Account toBeCreated = new Account.Builder()
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .username(registration.getEmail())
                .build();
        Account result = createAccountWithPassword(toBeCreated, registration.getPassword());
        mailService.sendPreConfiguredMail(MailTypeEnum.REGISTRATION, ImmutableMap.of(Constants
                .EMAIL_ACCOUNT_VARIABLE, result, Constants.EMAIL_BASE_URL_VARIABLE, baseUrl));
        return new AccountDTO(result.getId(), result.getUsername(), result.getFirstName(), result.getLastName(), result.getRole().name(), result.isActive());
    }

    @Override
    @Transactional(readOnly = false)
    public boolean firstTimeActivation(final long id, final boolean sendMail) {
        Account account = getAccount(id);
        account.setActive(true);
        accountDao.save(account);
        if (sendMail) {
            return mailService.sendMail(account.getUsername(),
                    messageSource.getMessage("email.activation.subject", null, Locale.ENGLISH),
                    MailTypeEnum.ACTIVATION, ImmutableMap.of(Constants.EMAIL_ACCOUNT_VARIABLE, account));
        }
        return true;
    }


    @Override
    @Transactional(readOnly = false)
    public void changeActivation(long id, boolean status) {
        Account account = getAccount(id);
        account.setActive(status);
        accountDao.save(account);
    }

    @Override
    @Transactional(readOnly = false)
    public void elevate(long id) {
        Account account = getAccount(id);
        account.setRole(Role.ADMIN);
        accountDao.save(account);
    }

    @Override
    @Transactional(readOnly = false)
    public void demote(long id) {
        Account account = getAccount(id);
        account.setRole(Role.USER);
        accountDao.save(account);
    }

    @Override
    @Transactional(readOnly = false)
    public void changeRole(AccountDTO accountDTO, Role role) {
        Account account = getAccount(accountDTO.getId());
        account.setRole(role);
        accountDao.save(account);
    }

    @Transactional(readOnly = false)
    public Account createAccountWithPassword(Account account, String password) {
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

    @Override
    public boolean isValidUsernameExcludeCurrentId(String username, Long id) {
        return accountDao.findByEmailExcludeCurrentId(username, id) == null;
    }

    @Override
    @Transactional(readOnly = false)
    public void setPasswordFor(long id, String password) {
        String encPassword = passwordEncoder.encode(password);
        jdbcTemplate.update(UPDATE_PASSWORD_SQL, encPassword, id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkOldPassword(long id, String password) {
        final Account account = getAccount(id);
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
        Account account = accountDao.findById(profileDTO.getId()).orElseThrow(() -> new ObjectNotFoundException(String.format("Account with id %s not found", profileDTO.getId())));
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
        return accountDao.findById(GeneralUtils.convertToLong(id)).orElseThrow(() -> new ObjectNotFoundException(String.format("Account with id %s not found", id)));
    }

    @Override
    public Account getAccount(Long id) {
        return accountDao.findById(id).orElseThrow(() -> new ObjectNotFoundException(String.format("Account with id %s not found", id)));
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
        account.getAccountSettings().setSendDoodleNotifications(profileDTO.isDoodleNotifications());
        account.getAccountSettings().setSendNewsNotifications(profileDTO.isNewsNotifications());
        //Set image as a null value to remove, else set profile image with provided image.
        //updatedAccount.getAccountProfile().setAvatar(form.isRemoveAvatar() ?
        //null :
        //createProfileImage(updatedAccount.getAccountProfile().getAvatar(), form.getAvatar()));
    }

    private Address createAddress(Address address, AddressDTO addressDTO) {
        if (addressDTO == null) return null;
        if (addressDTO.isFullAddress()) {
            if (address == null) address = new Address();
            address.setAddress(addressDTO.getAddress());
            address.setCity(addressDTO.getCity());
            address.setPostalCode(addressDTO.getPostalCode());
        }
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
