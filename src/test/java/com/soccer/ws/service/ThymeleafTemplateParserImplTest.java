package com.soccer.ws.service;

import com.google.common.collect.ImmutableMap;
import com.soccer.common.DataFactory;
import com.soccer.ws.configuration.TemplateConfig;
import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.News;
import com.soccer.ws.utils.Constants;
import com.soccer.ws.utils.ThymeleafTemplateParserImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.thymeleaf.TemplateEngine;

import java.util.UUID;

import static com.soccer.ws.utils.Constants.EMAIL_ACCOUNT_VARIABLE;
import static com.soccer.ws.utils.Constants.EMAIL_BASE_URL_VARIABLE;

/**
 * Created by u0090265 on 14.10.17.
 */
public class ThymeleafTemplateParserImplTest {
    private static final TemplateEngine templateEngine = new TemplateConfig(new StaticApplicationContext()).emailTemplateEngine();
    private static final Account account = DataFactory.createAccount();
    private static final String baseUrl = "https://base.com";

    ThymeleafTemplateParserImpl parser = new ThymeleafTemplateParserImpl(templateEngine);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void parsePasswordRecovery() throws Exception {
        parser.parse(MailTypeEnum.PASSWORD_RECOVERY, ImmutableMap.of(Constants.EMAIL_ACCOUNT_VARIABLE, account,
                "code", "code",
                Constants.EMAIL_BASE_URL_VARIABLE, baseUrl));
    }

    @Test
    public void parseMessage() throws Exception {
        News n = new News();
        n.setId(UUID.randomUUID());
        parser.parse(MailTypeEnum.MESSAGE, ImmutableMap.of("message", new News(), Constants.EMAIL_BASE_URL_VARIABLE,
                baseUrl));
        parser.parse(MailTypeEnum.MESSAGE, ImmutableMap.of("message", n, Constants.EMAIL_BASE_URL_VARIABLE,
                baseUrl));
    }

    @Test
    public void parseDoodleReserve() throws Exception {
        parser.parse(MailTypeEnum.DOODLE_RESERVE, ImmutableMap.of(Constants.EMAIL_ACCOUNT_VARIABLE, account));
    }

    @Test
    public void parseDoodleReminder() throws Exception {
        parser.parse(MailTypeEnum.DOODLE_REMINDER, ImmutableMap.of(EMAIL_ACCOUNT_VARIABLE, account,
                EMAIL_BASE_URL_VARIABLE, baseUrl));
    }

    @Test
    public void parseDoodleOpen() throws Exception {
        parser.parse(MailTypeEnum.DOODLE_OPEN, ImmutableMap.of(EMAIL_ACCOUNT_VARIABLE, account,
                EMAIL_BASE_URL_VARIABLE, baseUrl));
    }

    @Test
    public void parseRegistration() throws Exception {
        parser.parse(MailTypeEnum.REGISTRATION, ImmutableMap.of(Constants
                .EMAIL_ACCOUNT_VARIABLE, account, Constants.EMAIL_BASE_URL_VARIABLE, baseUrl));
    }

    @Test
    public void parseActivation() throws Exception {
        parser.parse(MailTypeEnum.ACTIVATION, ImmutableMap.of(Constants.EMAIL_ACCOUNT_VARIABLE, account));
    }

}
