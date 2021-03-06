package com.soccer.ws.utils;

import com.google.common.base.Strings;
import com.soccer.ws.exceptions.ObjectNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.UUID;

/**
 * Created by u0090265 on 9/22/14.
 */
public class GeneralUtils {
    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd/MM/YYYY");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/YYYY - HH:mm");
    private static final DateTimeFormatter hourFormatter = DateTimeFormat.forPattern("HH:mm");

    private static final DateTimeZone defaultTimeZone = DateTimeZone.forID("Europe/Brussels");

    public static Long convertToLong(String element) {
        return Long.parseLong(element);
    }

    public static UUID convertToUUID(String element) {
        return UUID.fromString(element);
    }

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateRandomHex(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

    public static String convertToStringDate(DateTime dateTime) {
        if (dateTime == null) return null;
        return dateFormatter.print(dateTime);
    }

    public static String convertToStringDateTimeWithDefaultTimeZone(DateTime dateTime) {
        if (dateTime == null) return null;
        return dateTimeFormatter.withZone(defaultTimeZone).print(dateTime);
    }

    public static String convertToStringDateTime(DateTime dateTime) {
        if (dateTime == null) return null;
        return dateTimeFormatter.print(dateTime);
    }

    public static String convertToStringDate(Date date) {
        if (date == null) return null;
        return dateFormatter.print(new DateTime(date));
    }

    public static DateTime convertToDate(String date) {
        return dateFormatter.parseDateTime(date);
    }

    public static DateTime convertToDate(String date, String time) {
        return dateTimeFormatter.parseDateTime(String.format("%s - %s", date, time));
    }

    public static String convertToStringHour(DateTime dateTime) {
        return hourFormatter.print(dateTime);
    }

    public static String trim(String input) {
        return input == null ? "" : input.trim();
    }

    public static void throwObjectNotFoundException(Object o, UUID id, Class expectedClass) {
        if (o == null) throw new ObjectNotFoundException(String.format("%s %s not found", expectedClass.getName(), id));
    }

    public static String abbreviateLastName(String name) {
        if (Strings.isNullOrEmpty(name)) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String p : name.split(" ")) {
            stringBuilder.append(p.substring(0, 1));
        }
        return stringBuilder.toString();
    }

    public static String abbreviateName(String firstName, String lastName) {
        StringBuilder b = new StringBuilder(firstName)
                .append(" ")
                .append(abbreviateLastName(lastName));
        return b.toString();

    }
}
