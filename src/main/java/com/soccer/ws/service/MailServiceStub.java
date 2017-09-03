package com.soccer.ws.service;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by u0090265 on 17.06.17.
 */
@Profile("default")
@Service
public class MailServiceStub implements MailService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean sendMail(String to, String name, String from, String subject, String body) {
        log(Lists.newArrayList(to), subject);
        return true;
    }

    @Override
    public boolean sendMail(String to, String subject, String body) {
        log(Lists.newArrayList(to), subject);
        return true;
    }

    @Override
    public boolean sendMail(String to, String name, String subject, String body) {
        log(Lists.newArrayList(to), subject);
        return true;
    }

    @Override
    public boolean sendMail(Set<String> to, String subject, String body) {
        log(Lists.newArrayList(to), subject);
        return true;
    }

    @Override
    public boolean sendPreConfiguredMail(String message) {
        return true;
    }

    private void log(List<String> to, String subject) {
        log.info(String.format("Sending email to %s with subject %s", to, subject));
    }
}
