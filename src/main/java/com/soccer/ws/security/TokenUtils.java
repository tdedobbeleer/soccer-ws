package com.soccer.ws.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by u0090265 on 17.03.17.
 */
public interface TokenUtils {
    Boolean validateToken(String token, UserDetails userDetails, String fingerPrint);

    String generateToken(UserDetails userDetails, String fingerPrint, boolean rememberMe);

    String getUsernameFromToken(String token);
}
