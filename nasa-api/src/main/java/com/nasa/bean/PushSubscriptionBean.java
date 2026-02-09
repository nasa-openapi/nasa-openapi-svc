package com.nasa.bean;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSubscriptionBean { // PUBLIC parent

    private String endpoint;
    private Keys keys;
    private String name;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Keys { // PUBLIC STATIC inner class
        private String p256dh; // PRIVATE fields
        private String auth;   // PRIVATE fields
    }
}
