package com.nasa.logging.service.impl;

import com.nasa.bean.PicOfDayTaskLogEvent;
import com.nasa.entity.PicOfDayLogEntity;
import com.nasa.repository.LogEntityRepository;
import com.nasa.service.IPicOfDayLogService;
import com.nasa.status.PicOfDayTaskStatus;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PicOfDayLogService implements IPicOfDayLogService {

    private final LogEntityRepository logRepository;

    public PicOfDayLogService(LogEntityRepository logRepository) {
        this.logRepository = logRepository;
    }



    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public void logSuccess(String source, Instant timestamp) {
        logRepository.save(buildLogEntity(source,null, PicOfDayTaskStatus.SUCCESS.name(), timestamp));
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public void logError(String source, String message, Instant timestamp) {
        logRepository.save(buildLogEntity(source,message,PicOfDayTaskStatus.ERROR.name(), timestamp));

    }


    @Override
    @EventListener
    @Async
    public void logMessage(PicOfDayTaskLogEvent payload) {
        switch(payload.getStatus()){
            case SUCCESS -> {
                logSuccess(payload.getSource(), payload.getRunAt());
            }
            case ERROR -> {
                logError(payload.getSource(), payload.getMessage(), payload.getRunAt());
            }
        }
    }

    public PicOfDayLogEntity buildLogEntity(String source, String message, String logType, Instant timestamp){
        return PicOfDayLogEntity.builder()
                .logMessage(message)
                .runTime(timestamp)
                .logType(logType)
                .source(source).build();

    }

}
