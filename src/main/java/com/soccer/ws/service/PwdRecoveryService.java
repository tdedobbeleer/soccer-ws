package com.soccer.ws.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * Created by u0090265 on 9/11/14.
 */

public interface PwdRecoveryService {
    void deleteExpiredCodes();

    void setRecoveryCodeAndEmail(String email, Locale locale);

    void checkPwdRecoverCodeAndEmail(String password, String email, String code);

    @Transactional
    boolean isValidRecoveryCode(String email, String code);
}
