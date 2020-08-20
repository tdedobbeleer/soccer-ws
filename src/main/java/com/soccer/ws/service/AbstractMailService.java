package com.soccer.ws.service;

import com.google.common.collect.ImmutableMap;
import com.soccer.ws.data.MailTypeEnum;

import java.util.Map;

public abstract class AbstractMailService implements MailService {
    protected final String defaultAdminFromTo;
    protected final String defaultAdminName;
    protected final String defaultAdminSubject;

    protected AbstractMailService(String defaultAdminFromTo, String defaultAdminName, String defaultAdminSubject) {
        this.defaultAdminFromTo = defaultAdminFromTo;
        this.defaultAdminName = defaultAdminName;
        this.defaultAdminSubject = defaultAdminSubject;
    }

    protected abstract boolean sendMessage(Map<String, String> to, String subject, MailTypeEnum type, Map<String, Object> propertyMap);

    @Override
    public boolean sendMail(String to, String name, String from, String subject, String body, MailTypeEnum type, Map<String,
            Object> propertyMap) {
        return sendMessage(ImmutableMap.of(to, name), subject, type, propertyMap);
    }

    @Override
    public boolean sendMail(String to, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        return sendMessage(ImmutableMap.of(to, ""), subject, type, propertyMap);
    }

    @Override
    public boolean sendMail(String to, String name, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        return sendMessage(ImmutableMap.of(to, name), subject, type, propertyMap);
    }

    @Override
    public void sendPreConfiguredMail(MailTypeEnum type, Map<String, Object> propertyMap) {
        sendMessage(ImmutableMap.of(defaultAdminFromTo, defaultAdminName), defaultAdminSubject, type, propertyMap);
    }
}
