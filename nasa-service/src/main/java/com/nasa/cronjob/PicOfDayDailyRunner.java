package com.nasa.cronjob;

import com.nasa.entity.PicOfDayEntity;
import com.nasa.exception.PicOfDayServiceException;
import com.nasa.service.IPicOfDayService;
import com.nasa.service.impl.PicOfDayLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Daily running cron job to pull in images from NASA
 * Also handles the corresponding logging. Logging may need to be moved
 * out to be handled using decorators
 */
@Component
@Profile("daily-job")
public class PicOfDayDailyRunner implements CommandLineRunner {

    @Autowired
    @Qualifier(IPicOfDayService.NAME)
    IPicOfDayService picOfDayService;

    @Autowired
    PicOfDayLogService logService;


    private static final Logger LOGGER = LoggerFactory.getLogger(PicOfDayDailyRunner.class);


    @Override
    public void run(String... args) throws Exception {
        LocalDateTime today = LocalDateTime.now();
        LOGGER.info("Kicking off daily run for day {}",today);
        try{
            PicOfDayEntity entity = picOfDayService.fetchTodaysPic();
            logService.logSuccess("DAILY_RUNNER");
        }catch (PicOfDayServiceException e){
            logService.logError("DAILY_RUNNER",e.getMessage());
            throw e;
        } catch (Exception e){
            LOGGER.error("FATAL ERROR: UNKNOWN ERROR -- ",e);
            logService.logError("DAILY_RUNNER",e.getMessage());
            throw e;
        }
        LOGGER.info("Completed daily run for {}",today);

    }
}
