package com.nasa.notification;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.push.vapid")
@Getter
@Setter
public class PushProperties {

    private String publicKey;
    private String privateKey;
    private String subject;
}
