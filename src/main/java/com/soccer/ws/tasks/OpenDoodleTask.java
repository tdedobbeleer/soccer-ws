package com.soccer.ws.tasks;

import com.google.common.collect.ImmutableMap;
import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.service.MailService;
import com.soccer.ws.service.MatchesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static com.soccer.ws.utils.Constants.EMAIL_ACCOUNT_VARIABLE;
import static com.soccer.ws.utils.Constants.EMAIL_BASE_URL_VARIABLE;

@Component
public class OpenDoodleTask implements Task {
    private final MatchesService matchesService;
    private final AccountService accountService;
    private final MailService mailService;
    private final String baseUrl;
    private final MessageSource messageSource;
    private final boolean sendDoodleOpenEmail;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public OpenDoodleTask(MatchesService matchesService, AccountService accountService, MailService mailService, @Value("${base.url}") String baseUrl, MessageSource messageSource, @Value("${matches.doodle.open.email}") boolean sendDoodleOpenEmail) {
        this.matchesService = matchesService;
        this.accountService = accountService;
        this.mailService = mailService;
        this.messageSource = messageSource;
        this.baseUrl = baseUrl;
        this.sendDoodleOpenEmail = sendDoodleOpenEmail;
    }

    @Override
    @Scheduled(cron = "${openDoodleTask.cronSchedule}", zone = "Europe/Brussels")
    public void execute() {
        log.info("Execute OpenDoodleTask - start");
        matchesService.openNextMatchDoodle().forEach(m -> {
            //only send email when prop is set
            if (sendDoodleOpenEmail) {
                log.debug("Match {} is opened, sending emails.", m.getDescription());
                String subject = messageSource.getMessage("email.doodle.open.subject", new String[]{m
                        .getDescription(), m.getStringDateTime()}, Locale.ENGLISH);
                accountService.getAllActivateAccounts().forEach(a -> {
                    log.debug("Sending doodle open email to {}.", a.getUsername());
                    mailService.sendMail(a.getUsername(), a.toString(), subject, MailTypeEnum
                            .DOODLE_OPEN, ImmutableMap.of(EMAIL_ACCOUNT_VARIABLE, a,
                            EMAIL_BASE_URL_VARIABLE, baseUrl));

                });
            }
        });
        log.info("Execute OpenDoodleTask - end");
    }
}
