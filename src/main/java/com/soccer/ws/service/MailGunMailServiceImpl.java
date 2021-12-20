package com.soccer.ws.service;

import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.utils.TemplateParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by u0090265 on 12/08/16.
 */
//@Profile("!default")
//@Service
public class MailGunMailServiceImpl extends AbstractMailService {

    private static final Logger logger = LoggerFactory.getLogger(MailGunMailServiceImpl.class);

    private final TemplateParser templateParser;
    private final String apiKey;
    private final String apiUrl;
    private WebResource defaultMessageWebResource;

    @Autowired
    public MailGunMailServiceImpl(TemplateParser templateParser,
                                  @Value("${mail.admin.fromTo}") String defaultAdminFromTo,
                                  @Value("${mail.admin.name}") String defaultAdminName,
                                  @Value("${mail.admin.subject}") String defaultAdminSubject,
                                  @Value("${mailgun.api.key}") String apiKey,
                                  @Value("${mailgun.api.url}") String apiUrl
    ) {
        super(defaultAdminFromTo, defaultAdminName, defaultAdminSubject);
        this.templateParser = templateParser;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
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

    protected boolean sendMessage(Map<String, String> to, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
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
}
