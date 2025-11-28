package com.nasa.cronjob;

import com.nasa.entity.PicOfDayEntity;
import com.nasa.exception.PicOfDayServiceException;
import com.nasa.service.IPicOfDayLogService;
import com.nasa.service.IPicOfDayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Daily running cron job to pull in images from NASA
 * Also handles the corresponding logging. Logging may need to be moved
 * out to be handled using decorators
 */
@Component
public class PicOfDayDailyRunner {

    @Autowired
    @Qualifier(IPicOfDayService.NAME)
    IPicOfDayService picOfDayService;

    @Autowired
    IPicOfDayLogService logService;


    private static final Logger LOGGER = LoggerFactory.getLogger(PicOfDayDailyRunner.class);


    @Scheduled(cron = "0 30 04 * * ?", zone  = "Europe/London")
    public void run() {
        Instant now = Instant.now();
        LOGGER.info("Kicking off daily run for day {}",now);
        try{
            PicOfDayEntity entity = picOfDayService.fetchTodaysPic();
            logService.logSuccess("DAILY_RUNNER", now);
        }catch (PicOfDayServiceException e){
            logService.logError("DAILY_RUNNER",e.getMessage(), now);
            throw e;
        } catch (Exception e){
            LOGGER.error("FATAL ERROR: UNKNOWN ERROR -- ",e);
            logService.logError("DAILY_RUNNER",e.getMessage(),now );
            throw e;
        }
        LOGGER.info("Completed daily run ");

    }
}
