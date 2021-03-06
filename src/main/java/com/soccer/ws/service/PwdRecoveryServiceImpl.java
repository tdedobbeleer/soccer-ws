package com.soccer.ws.service;

import com.google.common.collect.ImmutableMap;
import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.exceptions.EmailNotSentException;
import com.soccer.ws.exceptions.InvalidRecoveryCodeException;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.Account;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.utils.Constants;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by u0090265 on 9/11/14.
 */
@Service
@Transactional
public class PwdRecoveryServiceImpl implements PwdRecoveryService {

    private final static String TIMESTAMP_PATTERN = "yyyyMMddHHmmss";
    private static final int RECOVERY_LENGTH = 10;
    private static final Logger log = LoggerFactory.getLogger(PwdRecoveryServiceImpl.class);
    @Value("${base.url}")
    private String baseUrl;
    private MailService mailService;
    private AccountDao accountDao;
    private MessageSource messageSource;
    private AccountService accountService;

    @Autowired
    public PwdRecoveryServiceImpl(MailService mailService, AccountDao accountDao, MessageSource messageSource,
                                  AccountService accountService) {
        this.mailService = mailService;
        this.accountDao = accountDao;
        this.messageSource = messageSource;
        this.accountService = accountService;
    }

    @Override
    @Transactional
    public void deleteExpiredCodes() {
        List<Account> accounts = accountDao.findAllByPwdRecoveryNotNull();
        DateTime hourAgo = DateTime.now().minusHours(1);

        for (Account a : accounts) {

            DateTime codeTime = getDateFromDbString(a.getPwdRecovery());
            if (codeTime.isBefore(hourAgo)) {
                a.setPwdRecovery(null);
                accountDao.save(a);
                log.info("Deleted activationcode for account {}", a.getUsername());
            }
        }
    }

    @Override
    @Transactional
    public void setRecoveryCodeAndEmail(String email, Locale locale) {
        Account account = accountDao.findByUsernameIgnoreCase(email);
        if (account == null) throw new ObjectNotFoundException(String.format("Account with email %s not found", email));

        String recoveryHex = getRandomHexString(RECOVERY_LENGTH);
        String pwdRecoveryCode = getTimeStamp() + recoveryHex;

        account.setPwdRecovery(pwdRecoveryCode);

        accountDao.save(account);

        if (!mailService.sendMail(
                account.getUsername(),
                account.toString(),
                messageSource.getMessage("email.pwd.recovery.subject", null, locale),
                MailTypeEnum.PASSWORD_RECOVERY,
                ImmutableMap.of(Constants.EMAIL_ACCOUNT_VARIABLE, account,
                        "code", recoveryHex,
                        Constants.EMAIL_BASE_URL_VARIABLE, baseUrl))) {
            throw new EmailNotSentException("Email could not be sent");
        }
    }

    @Override
    @Transactional
    public void checkPwdRecoverCodeAndEmail(String password, String email, String code) {
        Account account = accountDao.findByUsernameIgnoreCase(email);
        if (account == null) throw new ObjectNotFoundException(String.format("Account with email %s not found", email));

        String dbRecoveryCode = getCodeFromDbString(account.getPwdRecovery());

        if (dbRecoveryCode == null || !dbRecoveryCode.equals(code)) {
            throw new InvalidRecoveryCodeException("Wrong recovery code");
        } else {
            account.setPwdRecovery(null);
            accountService.setPasswordFor(account.getId(), password);
        }
    }

    @Override
    @Transactional
    public boolean isValidRecoveryCode(String email, String code) {
        Account account = accountDao.findByUsernameIgnoreCase(email);
        if (account == null) throw new ObjectNotFoundException(String.format("Account with email %s not found", email));
        String dbRecoveryCode = getCodeFromDbString(account.getPwdRecovery());
        return !(dbRecoveryCode == null || !dbRecoveryCode.equals(code));
    }

    private String getTimeStamp() {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern(TIMESTAMP_PATTERN);
        return fmt.print(dt);
    }

    private DateTime getDateTimeFromString(String date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(TIMESTAMP_PATTERN);
        return formatter.parseDateTime(date);
    }

    private String getRandomHexString(int numChars) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < numChars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numChars);
    }

    private String getCodeFromDbString(String code) {
        if (code == null || code.length() < TIMESTAMP_PATTERN.length() + RECOVERY_LENGTH) return null;
        return code.substring(TIMESTAMP_PATTERN.length(), code.length());
    }

    private DateTime getDateFromDbString(String code) {
        if (code == null || code.length() < TIMESTAMP_PATTERN.length() + RECOVERY_LENGTH) return null;
        return getDateTimeFromString(code.substring(0, TIMESTAMP_PATTERN.length()));
    }

}
