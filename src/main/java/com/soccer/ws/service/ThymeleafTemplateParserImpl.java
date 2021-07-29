package com.soccer.ws.service;

import com.google.common.collect.Maps;
import com.soccer.ws.data.MailTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

/**
 * Created by u0090265 on 14.10.17.
 */
@Service
public class ThymeleafTemplateParserImpl implements TemplateParser {
    private final TemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(ThymeleafTemplateParserImpl.class);

    public ThymeleafTemplateParserImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        try {
            this.parse(MailTypeEnum.TEST, Maps.newHashMap());

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String parse(MailTypeEnum type, Map<String, Object> propertyMap) {
        final Context ctx = new Context(Locale.ENGLISH);
        propertyMap.forEach(ctx::setVariable);
        return this.templateEngine.process(type.getValue(), ctx);
    }
}
