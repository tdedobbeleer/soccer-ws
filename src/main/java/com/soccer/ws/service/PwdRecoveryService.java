package com.soccer.ws.service;

import java.util.Locale;

/**
 * Created by u0090265 on 9/11/14.
 */

public interface PwdRecoveryService {
    void deleteExpiredCodes();

    void setRecoveryCodeAndEmail(String email, Locale locale);

    void checkPwdRecoverCodeAndEmail(String password, String email, String code);
}
