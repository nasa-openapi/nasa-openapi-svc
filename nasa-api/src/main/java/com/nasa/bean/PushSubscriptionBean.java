package com.nasa.bean;

public class PushSubscriptionBean {


    private String endpoint;
    private Keys keys;

    public static class Keys {
        private String p256dh;
        private String auth;

        // Getters / setters
        public String getP256dh() { return p256dh; }
        public void setP256dh(String p256dh) { this.p256dh = p256dh; }

        public String getAuth() { return auth; }
        public void setAuth(String auth) { this.auth = auth; }
    }

    // Getters / setters
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public Keys getKeys() { return keys; }
    public void setKeys(Keys keys) { this.keys = keys; }

}
