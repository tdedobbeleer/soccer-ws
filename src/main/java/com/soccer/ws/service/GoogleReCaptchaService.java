package com.soccer.ws.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by u0090265 on 28/09/16.
 */
public interface GoogleReCaptchaService {
    boolean isResponseValid(HttpServletRequest servletRequest, String response);
}
