package com.nasa.util;

public class TimeZoneContext {
    private static final ThreadLocal<String> timezoneContext = new ThreadLocal<>();

    public static void setTimezone(String timezone) {
        timezoneContext.set(timezone);
    }

    public static String getTimezone() {
        return timezoneContext.get();
    }

    public static void clear() {
        timezoneContext.remove();
    }
}

