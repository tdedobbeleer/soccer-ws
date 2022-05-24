package com.soccer.ws.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.utils.TemplateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
@Lazy
@Profile("!default")
public class SendgridMailServiceImpl extends AbstractMailService {

    private static final Logger logger = LoggerFactory.getLogger(SendgridMailServiceImpl.class);

    private final TemplateParser templateParser;
    private final String apiKey;

    @Autowired
    public SendgridMailServiceImpl(TemplateParser templateParser,
                                   @Value("${mail.admin.fromTo}") String defaultAdminFromTo,
                                   @Value("${mail.admin.name}") String defaultAdminName,
                                   @Value("${mail.admin.subject}") String defaultAdminSubject,
                                   @Value("${sendgrid.api.key}") String apiKey
    ) {
        super(defaultAdminFromTo, defaultAdminName, defaultAdminSubject);
        this.templateParser = templateParser;
        this.apiKey = apiKey;
    }

    protected boolean sendMessage(Map<String, String> to, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        Email from = new Email(defaultAdminFromTo, defaultAdminName);
        Content content = new Content("text/html", templateParser.parse(type, propertyMap));

        for (Map.Entry<String, String> entry : to.entrySet()) {
            try {
                Mail mail = new Mail(from, subject, new Email(entry.getKey().isEmpty() ? entry.getValue()
                        : entry.getKey(), entry.getValue()), content);
                SendGrid sg = new SendGrid(apiKey);
                Request request = new Request();

                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                Response response = sg.api(request);

                logger.info(String.format("Result of sending email to %s with subject %s: %s", Arrays.toString(to.entrySet()
                        .toArray()), subject, response.getStatusCode()
                ));
            } catch (Exception e) {
                logger.error(String.format("Result of sending email to %s with subject %s: %s", Arrays.toString(to.entrySet()
                        .toArray()), subject, e.getMessage()
                ));
            }
        }
        return true;
    }
}
