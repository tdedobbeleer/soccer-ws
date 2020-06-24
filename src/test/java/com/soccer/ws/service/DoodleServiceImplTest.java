package com.soccer.ws.service;

import com.google.common.collect.Sets;
import com.soccer.common.DataFactory;
import com.soccer.common.JUnitTest;
import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.data.MatchStatusEnum;
import com.soccer.ws.exceptions.UnauthorizedAccessException;
import com.soccer.ws.model.*;
import com.soccer.ws.persistence.AccountDao;
import com.soccer.ws.persistence.DoodleDao;
import com.soccer.ws.persistence.MatchesDao;
import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Created by u0090265 on 07.10.17.
 */
public class DoodleServiceImplTest extends JUnitTest {
    private final DoodleDao doodleDao = EasyMock.createStrictMock(DoodleDao.class);
    private final AccountDao accountDao = EasyMock.createStrictMock(AccountDao.class);
    private final MatchesDao matchesDao = EasyMock.createStrictMock(MatchesDao.class);
    private final MailService mailService = EasyMock.createStrictMock(MailService.class);
    private final MessageSource messageSource = EasyMock.createStrictMock(MessageSource.class);

    private DoodleServiceImpl doodleService;

    @Before
    public void setUp() throws Exception {
        doodleService = new DoodleServiceImpl(doodleDao, accountDao, matchesDao, messageSource, mailService, 13, 1,
                "http://test.com");
        resetAll();
    }

    @Test
    public void testChangePresenceNormalUser() throws Exception {
        Match m = createTestMatch();
        Account a = DataFactory.createAccount();
        Doodle doodle = new Doodle();
        doodle.setStatus(DoodleStatusEnum.OPEN);
        m.setMatchDoodle(doodle);
        expect(accountDao.findById(a.getId())).andReturn(Optional.of(a));
        expect(matchesDao.findById(m.getId())).andReturn(Optional.of(m));
        expect(doodleDao.save(anyObject(Doodle.class))).andReturn(doodle);

        replayAll();

        Presence presence = doodleService.changePresence(a.getId(), m.getId(), false);

        assertEquals(a, presence.getAccount());
        assertTrue(presence.isPresent());

        verifyAll();
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void testChangePresenceNormalUserClosedDoodle() throws Exception {
        Match m = createTestMatch();
        Account a = DataFactory.createAccount();
        expect(accountDao.findById(a.getId())).andReturn(Optional.of(a));
        expect(matchesDao.findById(m.getId())).andReturn(Optional.of(m));
        expect(doodleDao.save(anyObject(Doodle.class))).andReturn(new Doodle());

        replayAll();

        Presence presence = doodleService.changePresence(a.getId(), m.getId(), false);

        verifyAll();
    }

    @Test
    public void testChangePresenceAdminUserMatchPlayed() throws Exception {
        Match m = createTestMatch();
        m.setStatus(MatchStatusEnum.PLAYED);
        Account a = DataFactory.createAccount();
        expect(accountDao.findById(a.getId())).andReturn(Optional.of(a));
        expect(matchesDao.findById(m.getId())).andReturn(Optional.of(m));
        expect(doodleDao.save(anyObject(Doodle.class))).andReturn(new Doodle());

        replayAll();

        Presence presence = doodleService.changePresence(a.getId(), m.getId(), true);

        assertEquals(a, presence.getAccount());
        assertTrue(presence.isPresent());

        verifyAll();
    }

    @Test
    public void testChangePresenceAdminUser() throws Exception {
        Match m = createTestMatch();
        m.setStatus(MatchStatusEnum.CANCELLED);
        Account a = DataFactory.createAccount();
        expect(accountDao.findById(a.getId())).andReturn(Optional.of(a));
        expect(matchesDao.findById(m.getId())).andReturn(Optional.of(m));
        expect(doodleDao.save(anyObject(Doodle.class))).andReturn(new Doodle());

        replayAll();

        Presence presence = doodleService.changePresence(a.getId(), m.getId(), true);

        assertEquals(a, presence.getAccount());
        assertTrue(presence.isPresent());

        verifyAll();
    }

    @Test
    public void testChangePresenceNormalUserAlreadyPresent() throws Exception {
        Match m = createTestMatch();
        Account a = DataFactory.createAccount();
        Doodle doodle = new Doodle();
        doodle.setStatus(DoodleStatusEnum.OPEN);
        m.setMatchDoodle(doodle);
        Presence p = new Presence();
        p.setPresent(true);
        p.setAccount(a);
        m.getMatchDoodle().setPresences(Sets.newHashSet(p));

        expect(accountDao.findById(a.getId())).andReturn(Optional.of(a));
        expect(matchesDao.findById(m.getId())).andReturn(Optional.of(m));
        expect(doodleDao.save(anyObject(Doodle.class))).andReturn(new Doodle());

        replayAll();

        Presence presence = doodleService.changePresence(a.getId(), m.getId(), false);

        assertEquals(a, presence.getAccount());
        assertFalse(presence.isPresent());

        verifyAll();
    }

    @Test
    public void testSendDoodleNotificationsForSuccess() throws Exception {
        Match m = createTestMatch();
        Account a = DataFactory.createAccount();
        a.setUsername("DidnotfillinDoodle");
        Account b = DataFactory.createAccount();
        b.setUsername("DisabledNotifications");
        Account c = DataFactory.createAccount();
        c.setUsername("IsPresent");
        Account d = DataFactory.createAccount();
        d.setUsername("IsReserve");
        b.getAccountSettings().setSendDoodleNotifications(false);

        Presence pc = new Presence();
        pc.setPresent(true);
        pc.setAccount(c);

        Presence pd = new Presence();
        pd.setPresent(true);
        pd.setReserve(true);
        pd.setAccount(d);

        m.getMatchDoodle().setPresences(Sets.newHashSet(pc, pd));

        //Expect the messageSource to be called
        expect(messageSource.getMessage(eq("email.doodle.subject"), anyObject(String[].class), eq(Locale.ENGLISH))).andReturn("Test");
        expect(mailService.sendMail(eq(a.getUsername()), eq(a.toString()), anyObject(String.class), eq(MailTypeEnum
                        .DOODLE_REMINDER),
                anyObject()))
                .andReturn
                        (true);

        replayAll();

        doodleService.sendDoodleNotificationsFor(m, Sets.newHashSet(a, b, c, d));

        verifyAll();
    }

    @Test
    public void testSendDoodleNotificationsForMatchTooFarAway() throws Exception {
        Match m = createTestMatch();
        m.setDate(DateTime.now().plusDays(14));
        Account a = DataFactory.createAccount();
        a.getAccountSettings().setSendDoodleNotifications(true);
        Account b = DataFactory.createAccount();

        replayAll();

        doodleService.sendDoodleNotificationsFor(m, Sets.newHashSet(a, b));

        verifyAll();
    }


    @Test
    public void testSendDoodleNotificationsEnoughPresences() throws Exception {
        Match m = createTestMatch();
        m.getMatchDoodle().setPresences(Sets.newHashSet(DataFactory.getPresences(13)));
        Account a = DataFactory.createAccount();
        a.getAccountSettings().setSendDoodleNotifications(true);
        Account b = DataFactory.createAccount();

        replayAll();

        doodleService.sendDoodleNotificationsFor(m, Sets.newHashSet(a, b));

        verifyAll();
    }

    private Match createTestMatch() {
        Match m = DataFactory.createMatch();
        m.setId(1L);
        return m;
    }

    @Override
    protected Object[] getMocks() {
        return new Object[]{doodleDao, accountDao, matchesDao, messageSource, mailService};
    }

}
