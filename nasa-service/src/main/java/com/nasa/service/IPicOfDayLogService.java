package com.nasa.service;

public interface IPicOfDayLogService {

    /**
     * log a successful entry to the picofdaytable
     * @param source - ui, api , cron job
     */
     void logSuccess(String source);

    /**
     * log an unsucessful fetch for picfoday
     * @param source - ui, api, cron job
     * @param message - exceptions, error messages
     */
     void logError(String source, String message);
}
