package com.soccer.ws.utils;

import com.soccer.ws.data.MailTypeEnum;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

/**
 * Created by u0090265 on 14.10.17.
 */
public class ThymeleafTemplateParserImpl implements TemplateParser {
    private final TemplateEngine templateEngine;

    public ThymeleafTemplateParserImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String parse(MailTypeEnum type, Map<String, Object> propertyMap) {
        final Context ctx = new Context(Locale.ENGLISH);
        propertyMap.forEach(ctx::setVariable);
        return this.templateEngine.process(type.getValue(), ctx);
    }
}
