package com.nasa.util;

import java.util.Optional;

public class TimeZoneContext {
    private static final ThreadLocal<String> timezoneContext = new ThreadLocal<>();

    public static void setTimezone(String timezone) {
        timezoneContext.set(timezone);
    }

    public static String getTimezone() {
        return Optional.ofNullable(timezoneContext.get()).orElse("UTC");
    }

    public static void clear() {
        timezoneContext.remove();
    }
}

