package com.soccer.ws.service;

import com.google.common.collect.Lists;
import com.soccer.ws.data.MailTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by u0090265 on 17.06.17.
 */
@Profile("default")
@Service
public class MailServiceStub implements MailService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TemplateParser templateParser;

    public MailServiceStub(TemplateParser templateParser) {
        this.templateParser = templateParser;
    }

    private void log(List<String> to, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        log.debug(String.format("Sending email to %s with subject %s:\n\n%s", to, subject, templateParser.parse(type,
                propertyMap
        )));
    }

    @Override
    public boolean sendMail(String to, String name, String from, String subject, String body, MailTypeEnum type, Map<String, Object> propertyMap) {
        log(Lists.newArrayList(to), subject, type, propertyMap);
        return true;
    }

    @Override
    public boolean sendMail(String to, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        log(Lists.newArrayList(to), subject, type, propertyMap);
        return true;
    }

    @Override
    public boolean sendMail(String to, String name, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        log(Lists.newArrayList(to), subject, type, propertyMap);
        return true;
    }

    @Override
    public boolean sendMail(Set<String> to, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        log(Lists.newArrayList(to), subject, type, propertyMap);
        return true;
    }

    @Override
    public boolean sendPreConfiguredMail(MailTypeEnum type, Map<String, Object> propertyMap) {
        log(Lists.newArrayList("admin"), "admin", type, propertyMap);
        return true;
    }
}
