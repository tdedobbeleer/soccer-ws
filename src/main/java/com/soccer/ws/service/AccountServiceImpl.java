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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final String UPDATE_PASSWORD_SQL = "UPDATE account SET password = :password WHERE ID = :id";
    private static final String GET_PASSWORD = "SELECT password FROM account WHERE id = :id";
    private final MessageSource messageSource;
    private final AccountDao accountDao;
    private final MailService mailService;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final DTOConversionHelper dtoConversionHelper;
    @Value("${base.url}")
    private String baseUrl;

    @Autowired
    public AccountServiceImpl(MessageSource messageSource, AccountDao accountDao, MailService mailService, NamedParameterJdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, ImageService imageService, DTOConversionHelper dtoConversionHelper) {
        this.messageSource = messageSource;
        this.accountDao = accountDao;
        this.mailService = mailService;
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.dtoConversionHelper = dtoConversionHelper;
    }

    @Override
    @Transactional
    public AccountDTO register(RegistrationDTO registration) {
        Account toBeCreated = new Account.Builder()
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .username(registration.getEmail())
                .build();
        Account result = createAccount(toBeCreated);
        mailService.sendPreConfiguredMail(MailTypeEnum.REGISTRATION, ImmutableMap.of(Constants
                .EMAIL_ACCOUNT_VARIABLE, result, Constants.EMAIL_BASE_URL_VARIABLE, baseUrl));
        return new AccountDTO(result.getId(), result.getUsername(), result.getFirstName(), result.getLastName(), result.getRole().name(), result.isActive());
    }

    @Override
    @Transactional
    public boolean firstTimeActivation(final UUID id, final boolean sendMail) {
        Account account = accountDao.findById(id).orElse(null);
        if (account == null) throw new ObjectNotFoundException(String.format("Object with id %s not found", id));
        account.setActive(true);
        accountDao.save(account);
        if (sendMail) {
            if (!mailService.sendMail(account.getUsername(), account.getFullName(),
                    messageSource.getMessage("email.activation.subject", null, Locale.ENGLISH),
                    MailTypeEnum.ACTIVATION, ImmutableMap.of(Constants.EMAIL_ACCOUNT_VARIABLE, account))) {
                return false;
            }
        }
        return true;
    }


    @Override
    @Transactional
    public void changeActivation(UUID id, boolean status) {
        Account account = accountDao.findById(id).orElse(null);
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        account.setActive(status);
        accountDao.save(account);
    }

    @Override
    @Transactional
    public void elevate(UUID id) {
        Account account = accountDao.findById(id).orElse(null);
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        account.setRole(Role.ADMIN);
        accountDao.save(account);
    }

    @Override
    @Transactional
    public void demote(UUID id) {
        Account account = accountDao.findById(id).orElse(null);
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        account.setRole(Role.USER);
        accountDao.save(account);
    }

    @Override
    @Transactional
    public void changeRole(AccountDTO accountDTO, Role role) {
        Account account = accountDao.findById(accountDTO.getId()).orElse(null);
        if (account == null)
            throw new ObjectNotFoundException("Account not found");
        account.setRole(role);
        accountDao.save(account);
    }

    @Transactional
    public Account createAccount(Account account) {
        return accountDao.save(account);
    }

    @Transactional(readOnly = true)
    public boolean isValidUsername(String username) {
        return accountDao.findByUsername(username) == null;
    }

    @Override
    public boolean isValidUsernameExcludeCurrentId(String username, UUID id) {
        return accountDao.findByEmailExcludeCurrentId(username, id) == null;
    }

    @Override
    @Transactional
    public void setPasswordFor(UUID id, String password) {
        String encPassword = passwordEncoder.encode(password);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id).addValue("password", encPassword);
        int i = jdbcTemplate.update(UPDATE_PASSWORD_SQL, namedParameters);
        if (i != 1) throw new ObjectNotFoundException("Account not found, password was not set.");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkOldPassword(UUID id, String password) {
        final Account account = accountDao.findById(id).orElse(null);
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
    @Transactional
    public Account update(ProfileDTO profileDTO) {
        Account account = accountDao.findById(profileDTO.getId()).orElse(null);
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
        return accountDao.findById(GeneralUtils.convertToUUID(id)).orElse(null);
    }

    @Override
    public Account getAccount(UUID id) {
        return accountDao.findById(id).orElse(null);
    }

    @Override
    public Account getActiveAccountByEmail(String email) {
        return accountDao.findByUsernameAndActiveStatus(email, true);
    }

    @Override
    public Account getActiveAccountById(String id) {
        return accountDao.findByIdAndActiveStatus(GeneralUtils.convertToUUID(id), true);
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
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", account.getId());
        return jdbcTemplate.queryForObject(GET_PASSWORD, namedParameters, String.class);
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
        account.setUsername(profileDTO.getAccount().getUsername());
        account.setFirstName(profileDTO.getAccount().getFirstName());
        account.setLastName(profileDTO.getAccount().getLastName());
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
