package com.soccer.ws.validators;

import com.google.common.base.Strings;
import org.owasp.html.Sanitizers;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

/**
 * Created by u0090265 on 12/31/14.
 */
public class SanitizeUtils {
    public static String sanitizeHtml(String text) {
        if (Strings.isNullOrEmpty(text)) return text;
        String r = Sanitizers.FORMATTING.and(Sanitizers.IMAGES).and(Sanitizers.LINKS).and(Sanitizers.BLOCKS).sanitize
                (unescapeHtml4(text));
        return unescapeHtml4(r);
    }
}
