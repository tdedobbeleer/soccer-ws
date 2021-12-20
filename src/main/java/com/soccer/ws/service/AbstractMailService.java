package com.soccer.ws.service;

import com.google.common.collect.ImmutableMap;
import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.model.Account;
import com.soccer.ws.model.Role;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractMailService implements MailService {
    protected final String defaultAdminFromTo;
    protected final String defaultAdminName;
    protected final String defaultAdminSubject;
    protected final CacheAdapter cacheAdapter;

    protected AbstractMailService(String defaultAdminFromTo, String defaultAdminName, String defaultAdminSubject, CacheAdapter cacheAdapter) {
        this.defaultAdminFromTo = defaultAdminFromTo;
        this.defaultAdminName = defaultAdminName;
        this.defaultAdminSubject = defaultAdminSubject;
        this.cacheAdapter = cacheAdapter;
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
        //send an email to the Admins
        Map<String, String> admins = cacheAdapter.getActiveAccounts().stream().filter(a -> a.getRole().equals(Role.ADMIN)).collect(Collectors.toMap(Account::getUsername, Account::getFullName));
        sendMessage(admins, defaultAdminSubject, type, propertyMap);
    }
}
