package com.nasa.config;

import com.nasa.notification.PushProperties;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import com.nasa.interceptor.TimeZoneInterceptor;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final int TIMEOUT_VALUE = 20_000;

    static {
        // Add Bouncy Castle provider
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

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


    @Bean
    public PushService pushService(PushProperties props) throws GeneralSecurityException {
        return new PushService(
                props.getPublicKey(),
                props.getPrivateKey(),
                props.getSubject());
    }

}

