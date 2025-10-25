package com.nasa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nasa.interceptor.TimeZoneInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final int TIMEOUT_VALUE = 20_000;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TimeZoneInterceptor())
                .addPathPatterns("/**"); // Apply to all endpoints or specific ones
    }

    @Bean
    public RestTemplate restTemplate(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(TIMEOUT_VALUE);
        factory.setReadTimeout(TIMEOUT_VALUE);
        return new RestTemplate(factory);
    }
}

