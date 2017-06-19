package com.soccer.ws.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by u0090265 on 17.06.17.
 */
@Profile("default")
@Service
public class MailServiceStub implements MailService {
    @Override
    public boolean sendMail(String to, String name, String from, String subject, String body) {
        return true;
    }

    @Override
    public boolean sendMail(String to, String subject, String body) {
        return true;
    }

    @Override
    public boolean sendMail(String to, String name, String subject, String body) {
        return true;
    }

    @Override
    public boolean sendMail(Set<String> to, String subject, String body) {
        return true;
    }

    @Override
    public boolean sendPreConfiguredMail(String message) {
        return true;
    }
}
