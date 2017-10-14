package com.soccer.ws.utils;

/**
 * User: Tom De Dobbeleer
 * Date: 12/11/13
 * Time: 3:11 PM
 * Remarks: none
 */
public class Constants {
    public static final String PASSWORD_REGEX = "^[0-9a-zA-Z\\._-]{5,15}$";
    public static final String PHONE_REGEX = "^[0-9+]{9,13}$";
    public static final String EMAIL_REGEX = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}";
    public static final String NAME_REGEX = "^[\\p{IsLatin}-\\s']+$";
    public static final String TIME24HOURS_REGEX = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static final int MINUS_TEN = -10;
    public static final int TEN = 10;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int ZERO = 0;

    public static final String EMAIL_ACCOUNT_VARIABLE = "account";
    public static final String EMAIL_BASE_URL_VARIABLE = "baseUrl";

    private Constants() {
    }
}
