package com.vernonliu.authserver.core.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    public SecurityConfig() {
        super();
    }

    @Bean
    public WebMvcConfigurer corsConfiguration() throws Exception {
        String webAppOrigin = System.getenv("AUTH_WEBAPP_ORIGIN");
        if (StringUtils.isEmpty(webAppOrigin)) throw new Exception("Environment variable 'AUTH_WEBAPP_ORIGIN' missing");

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(webAppOrigin);
            }
        };
    }
}
