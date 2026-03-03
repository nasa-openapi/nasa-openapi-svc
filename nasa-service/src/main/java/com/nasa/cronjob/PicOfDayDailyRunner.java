package com.nasa.cronjob;

import com.nasa.bean.PicOfDayTaskLogEvent;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.exception.PicOfDayServiceException;
import com.nasa.service.IPicOfDayLogService;
import com.nasa.service.IPicOfDayService;
import com.nasa.status.PicOfDayTaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Daily running cron job to pull in images from NASA
 * Also handles the corresponding logging. Logging may need to be moved
 * out to be handled using decorators
 */
@Component
public class PicOfDayDailyRunner {


    private final IPicOfDayService picOfDayService;


    private final IPicOfDayLogService logService;

    private final ApplicationEventPublisher publisher;

    public PicOfDayDailyRunner(@Qualifier(IPicOfDayService.NAME) IPicOfDayService picOfDayService,
                               IPicOfDayLogService logService, ApplicationEventPublisher publisher){
        this.picOfDayService = picOfDayService;
        this.publisher = publisher;
        this.logService = logService;
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(PicOfDayDailyRunner.class);


    @Scheduled(cron = "0 00 15 * * ?")
    public void run() {
        Instant now = Instant.now();
        PicOfDayEntity entity = null;
        String message = null;
        PicOfDayTaskStatus status = null;
        LOGGER.info("Kicking off daily run for day {}",now);
        try{
            entity = picOfDayService.fetchTodaysPic();
            status = PicOfDayTaskStatus.SUCCESS;
        }catch (Exception e){
            status = PicOfDayTaskStatus.ERROR;
            message = e.getMessage();
            if (!(e instanceof PicOfDayServiceException )){
                LOGGER.error("FATAL ERROR: UNKNOWN ERROR -- ",e);
            }
            throw e;
        } finally {
            Integer entityID = entity ==null? null: entity.getId();
            publisher.publishEvent(new PicOfDayTaskLogEvent("DAILY_RUNNER",message, entityID, now, status));
        }
        LOGGER.info("Completed daily run ");

    }
}
