package com.soccer.ws.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by u0090265 on 27/11/16.
 */
@Configuration
public class WebAppConfig {
    @Value("${security.allowed.origin}")
    private String allowedOrigin;
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //Allow cors for localhost
                registry.addMapping("/**").allowedOrigins(allowedOrigin);
            }
        };
    }
}
