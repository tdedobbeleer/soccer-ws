package com.soccer.ws.service;

import com.google.common.base.Strings;
import com.soccer.ws.dto.ReCaptchaResponseDTO;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by u0090265 on 28/09/16.
 */
@Service
public class GoogleReCaptchaServiceImpl implements GoogleReCaptchaService {
    private final RestTemplate restTemplate;
    private final String reCaptchaUrl;
    private final String reCaptchaSecretKey;

    @Autowired
    public GoogleReCaptchaServiceImpl(@Value("${recaptcha.url}") String reCaptchaUrl,
                                      @Value("${recaptcha.secret.key}") String reCaptchaSecretKey) {
        this.restTemplate = new RestTemplate(
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build())
        );
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        this.reCaptchaSecretKey = reCaptchaSecretKey;
        this.reCaptchaUrl = reCaptchaUrl;
    }

    @Override
    public boolean isResponseValid(HttpServletRequest servletRequest, String response) {
        try {
            return restTemplate.postForEntity(
                    reCaptchaUrl, createBody(reCaptchaSecretKey, getRemoteIp(servletRequest), response),
                    ReCaptchaResponseDTO.class).getBody().isSuccess();
        } catch (RestClientException e) {
            throw new RuntimeException("Recaptcha API not available due to exception", e);
        }
    }

    private MultiValueMap<String, String> createBody(String secret, String remoteIp, String response) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("remoteip", remoteIp);
        form.add("response", response);
        return form;
    }

    private String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
