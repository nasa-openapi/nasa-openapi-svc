package com.nasa.service;

import java.time.Instant;

public interface IPicOfDayLogService {

    /**
     * log a successful entry to the picofdaytable
     *
     * @param source    - ui, api , cron job
     * @param timestamp
     */
     void logSuccess(String source, Instant timestamp);

    /**
     * log an unsucessful fetch for picfoday
     *
     * @param source    - ui, api, cron job
     * @param message   - exceptions, error messages
     * @param timestamp
     */
     void logError(String source, String message, Instant timestamp);
}
