package com.soccer.ws.service;

import com.soccer.ws.data.MailTypeEnum;

import java.util.Map;
import java.util.Set;

/**
 * Created by u0090265 on 9/11/14.
 */
public interface MailService {

    boolean sendMail(String to, String name, String from, String subject, String body, MailTypeEnum type, Map<String,
            Object> propertyMap);

    boolean sendMail(String to, String subject, MailTypeEnum type, Map<String, Object> propertyMap);

    boolean sendMail(String to, String name, String subject, MailTypeEnum type, Map<String, Object> propertyMap);

    boolean sendMail(Set<String> to, String subject, MailTypeEnum type, Map<String, Object> propertyMap);

    boolean sendPreConfiguredMail(MailTypeEnum type, Map<String, Object> propertyMap);
}
