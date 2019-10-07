package com.soccer.ws.service;

import com.google.common.collect.ImmutableMap;
import com.soccer.ws.data.MailTypeEnum;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by u0090265 on 12/08/16.
 */
@Profile("!default")
@Service
public class MailGunMailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailGunMailServiceImpl.class);

    private final TemplateParser templateParser;
    @Value("${mail.admin.fromTo}")
    private String defaultAdminFromTo;
    @Value("${mail.admin.name}")
    private String defaultAdminName;
    @Value("${mail.admin.subject}")
    private String defaultAdminSubject;
    @Value("${mailgun.api.key}")
    private String apiKey;
    @Value("${mailgun.api.url}")
    private String apiUrl;
    private WebResource defaultMessageWebResource;

    @Autowired
    public MailGunMailServiceImpl(TemplateParser templateParser) {
        this.templateParser = templateParser;
    }

    @PostConstruct
    private void create() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api",
                apiKey));
        defaultMessageWebResource =
                client.resource(apiUrl +
                        "/messages");
    }

    private boolean sendMessage(Map<String, String> to, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", String.format("%s <%s>", defaultAdminName, defaultAdminFromTo));
        formData.add("subject", subject);
        formData.add("html", templateParser.parse(type, propertyMap));

        logger.debug(String.format("Trying to send email to %s with subject %s", Arrays.toString(to.entrySet()
                .toArray()), subject
        ));

        for (Map.Entry<String, String> entry : to.entrySet()) {

            formData.add("to", String.format("%s <%s>", entry.getValue(), entry.getKey().isEmpty() ? entry.getValue()
                    : entry.getKey()));
        }

        ClientResponse c = defaultMessageWebResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        logger.info(String.format("Result of sending email to %s with subject %s: %s", Arrays.toString(to.entrySet()
                .toArray()), subject, c
        ));
        return c.getStatus() == 200;
    }

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
    public boolean sendPreConfiguredMail(MailTypeEnum type, Map<String, Object> propertyMap) {
        return sendMessage(ImmutableMap.of(defaultAdminFromTo, defaultAdminName), defaultAdminSubject, type, propertyMap);
    }
}
