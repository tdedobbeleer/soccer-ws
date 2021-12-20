package com.soccer.ws.service;

import com.soccer.ws.data.MailTypeEnum;

import java.util.Map;

/**
 * Created by u0090265 on 9/11/14.
 */
public interface MailService {

    boolean sendMail(String to, String name, String from, String subject, String body, MailTypeEnum type, Map<String,
            Object> propertyMap);

    boolean sendMail(String to, String subject, MailTypeEnum type, Map<String, Object> propertyMap);

    boolean sendMail(String to, String name, String subject, MailTypeEnum type, Map<String, Object> propertyMap);

    void sendPreConfiguredMail(Map<String, String> admins, MailTypeEnum type, Map<String, Object> propertyMap);
}
