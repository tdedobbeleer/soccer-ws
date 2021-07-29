package com.soccer.ws.service;

import com.soccer.ws.data.MailTypeEnum;
import com.soccer.ws.utils.TemplateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.SmtpApi;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Profile("!default")
public class SendInBlueMailService extends AbstractMailService {
    private static final Logger logger = LoggerFactory.getLogger(SendInBlueMailService.class);
    private final TemplateParser templateParser;

    @Autowired
    public SendInBlueMailService(@Value("${sendinblue.api.key}") String apiKey,
                                 @Value("${mail.admin.fromTo}") String defaultAdminFromTo,
                                 @Value("${mail.admin.name}") String defaultAdminName,
                                 @Value("${mail.admin.subject}") String defaultAdminSubject,
                                 TemplateParser templateParser) {
        super(defaultAdminFromTo, defaultAdminName, defaultAdminSubject);
        this.templateParser = templateParser;
        ((ApiKeyAuth) Configuration.getDefaultApiClient().getAuthentication("api-key")).setApiKey(apiKey);
    }

    protected boolean sendMessage(Map<String, String> to, String subject, MailTypeEnum type, Map<String, Object> propertyMap) {
        SmtpApi apiInstance = new SmtpApi();
        SendSmtpEmail email = new SendSmtpEmail();
        SendSmtpEmailSender sendSmtpEmailSender = new SendSmtpEmailSender();
        sendSmtpEmailSender.setEmail(defaultAdminFromTo);
        sendSmtpEmailSender.setName(defaultAdminName);
        email.setHtmlContent(templateParser.parse(type, propertyMap));
        email.setSender(sendSmtpEmailSender);
        email.setSubject(subject);
        email.setTo(to.entrySet().stream().map(m -> {
            SendSmtpEmailTo sendSmtpEmailTo = new SendSmtpEmailTo();
            sendSmtpEmailTo.setEmail(m.getKey());
            if (!m.getKey().isEmpty()) sendSmtpEmailTo.setName(m.getValue());
            return sendSmtpEmailTo;
        }).collect(Collectors.toList()));

        try {
            logger.debug(String.format("Trying to send email to %s with subject %s", Arrays.toString(to.entrySet()
                    .toArray()), subject
            ));
            apiInstance.sendTransacEmail(email);
            logger.info(String.format("Sending email to %s with subject %s: SUCCESS", Arrays.toString(to.entrySet()
                    .toArray()), subject
            ));
            return true;
        } catch (ApiException e) {
            logger.error(String.format("Sending email to %s with subject %s: FAILED", Arrays.toString(to.entrySet()
                    .toArray()), subject
            ));
            e.printStackTrace();
            return false;
        }
    }


}
