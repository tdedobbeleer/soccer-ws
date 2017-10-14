package com.soccer.ws.service;

import com.soccer.ws.data.MailTypeEnum;

import java.util.Map;

/**
 * Created by u0090265 on 14.10.17.
 */
public interface TemplateParser {
    String parse(MailTypeEnum type, Map<String, Object> propertyMap);
}
