package com.soccer.ws.service;

import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import com.soccer.ws.model.*;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.persistence.DoodleDao;
import com.soccer.ws.persistence.MatchesDao;
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

import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Created by u0090265 on 10/1/14.
 */
@Service
@Transactional
public class DoodleServiceImpl implements DoodleService {

    private final DoodleDao doodleDao;
    private final AccountDao accountDao;
    private final MatchesDao matchesDao;
    private final MessageSource messageSource;
    private final MailService mailService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${doodle.limit}")
    private int doodleLimit;

    @Value("${base.url}")
    private String baseUrl;

    @Autowired
    public DoodleServiceImpl(DoodleDao doodleDao, AccountDao accountDao, MatchesDao
            matchesDao, MessageSource messageSource, MailService mailService) {
        this.doodleDao = doodleDao;
        this.accountDao = accountDao;
        this.matchesDao = matchesDao;
        this.mailService = mailService;
        this.messageSource = messageSource;
    }

    @Override
    public Presence changePresence(final long accountId, final long matchId, final boolean isAdmin) {
        Account accountInUse = accountDao.findOne(accountId);
        if (accountInUse == null)
            throw new ObjectNotFoundException(String.format("Account with id %s not found.", accountId));

        Match match = matchesDao.findOne(matchId);
        if (match == null) throw new ObjectNotFoundException(String.format("Match with id %s not found.", matchId));
        if (!isAdmin && !match.getStatus().equals(MatchStatusEnum.NOT_PLAYED))
            throw new RuntimeException(String.format("Altering match with id %s not succeeded, match is " +
                    "finished/Cancelled.", matchId));
        Doodle d = match.getMatchDoodle();

        Presence presence = null;
        for (Presence p : d.getPresences()) {
            if (p.getAccount().equals(accountInUse)) {
                presence = p;
                break;
            }
        }
        presence = changePresence(d, presence, accountInUse, false);
        doodleDao.save(d);
        return presence;
    }

    @Override
    public Presence forceChangePresence(final long accountId, final long matchId) {
        Account accountInUse = accountDao.findOne(accountId);
        if (accountInUse == null)
            throw new ObjectNotFoundException(String.format("Account with id %s not found.", accountId));

        Match match = matchesDao.findOne(matchId);
        if (match == null) throw new ObjectNotFoundException(String.format("Match with id %s not found.", matchId));
        Doodle d = match.getMatchDoodle();

        Presence presence = null;
        for (Presence p : d.getPresences()) {
            if (p.getAccount().equals(accountInUse)) {
                presence = p;
                break;
            }
        }
        presence = changePresence(d, presence, accountInUse, true);
        doodleDao.save(d);
        return presence;
    }

    @Override
    public boolean sendDoodleNotificationsFor(Match match, Set<Account> accounts) {
        //Do nothing if object are null or empty
        if (match == null || accounts == null || accounts.isEmpty()) return false;
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        String matchDate = dtf.print(match.getDate());

        //Make sure the next match is this week and there are less than 13 players
        if (match.getMatchDoodle().countPresences() < 13 && match.getDate().weekOfWeekyear().equals(DateTime.now()
                .weekOfWeekyear())) {
            //Loop all active accounts
            for (Account account : accounts) {
                //Make sure they enabled the notifications
                if (account.getAccountSettings().isSendDoodleNotifications()) {
                    //If they didn't fill in the doodle, send mail
                    if (match.getMatchDoodle().getPresenceType(account).equals(Presence.PresenceType.NOT_FILLED_IN)) {
                        String subject = messageSource.getMessage("email.doodle.subject", new String[]{match
                                .getDescription(), matchDate}, Locale.ENGLISH);
                        String body = messageSource.getMessage("email.doodle.body", new String[]{account.getFirstName
                                (), baseUrl}, Locale.ENGLISH);
                        log.info("Account {} has not filled in doodle. Mail will be sent.", account.getUsername());
                        mailService.sendMail(account.getUsername(), account.toString(), subject, body);
                    } else {
                        log.info("Account {} has filled in doodle", account.getUsername());
                    }
                } else {
                    log.info("Account {} has disabled doodle notifications, not sending email", account.getUsername());
                }
            }
        } else {
            log.info("Match id {} not starting this week or has enough presences, aborting", match.getId());
        }
        return true;
    }

    private Presence changePresence(Doodle doodle, Presence p, Account account, boolean force) {
        //If not created, create and set present
        if (p == null) {
            p = new Presence();
            p.setAccount(account);
            doodle.getPresences().add(p);
            log.info(String.format("New presence created for account %s and doodle id %s", account.getUsername(),
                    doodle.getId()));
            determinePresenceType(doodle, p, true, force);
        } else {
            //Otherwise, set the opposite
            determinePresenceType(doodle, p, !p.isPresent(), force);
        }
        return p;
    }

    private void determinePresenceType(final Doodle doodle, final Presence presence, boolean present, boolean force) {
        if (!force && doodle.countPresences() >= doodleLimit) {
            if (present) {
                presence.setReserve(true);
            } else if (!presence.isReserve()) {
                notifyFirstReserve(doodle);
            } else {
                presence.setReserve(false);
            }
        }
        //If the status is forced, remove reserve status
        if (force) presence.setReserve(false);
        presence.setPresent(present);
    }

    private void notifyFirstReserve(Doodle doodle) {
        doodle.getPresences()
                .stream()
                .filter(Presence::isReserve)
                .sorted(Comparator.comparing(BaseClass::getModified))
                .findFirst()
                .ifPresent(p -> {
                    Optional<Match> m = matchesDao.findByMatchDoodle(doodle);
                    final Account account = p.getAccount();
                    //Set present
                    p.setPresent(true);
                    p.setReserve(false);
                    assert m.isPresent() : "A doodle without a match cannot exist";
                    log.info(String.format("Account %s is now set as present", account.getUsername()));
                    final String subject = messageSource.getMessage("email.doodle.reserve.subject", new Object[]{m.get().getDescription(), m.get().getStringDate() + " " + m.get().getStringHour()}, Locale.ENGLISH);
                    final String body = messageSource.getMessage("email.doodle.reserve.body", new Object[]{account.getFirstName()}, Locale.ENGLISH);
                    mailService.sendMail(account.getUsername(), account.toString(), subject, body);
                });
    }
}
