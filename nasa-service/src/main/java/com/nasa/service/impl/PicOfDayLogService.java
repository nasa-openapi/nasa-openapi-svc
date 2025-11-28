package com.nasa.service.impl;

import com.nasa.entity.PicOfDayLogEntity;
import com.nasa.repository.LogEntityRepository;
import com.nasa.service.IPicOfDayLogService;
import jakarta.transaction.Transactional;
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
        logRepository.save(buildLogEntity(source,null,"SUCCESS", timestamp));
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public void logError(String source, String message, Instant timestamp) {
        logRepository.save(buildLogEntity(source,message,"ERROR", timestamp));

    }

    public PicOfDayLogEntity buildLogEntity(String source, String message, String logType, Instant timestamp){
        return PicOfDayLogEntity.builder()
                .logMessage(message)
                .runTime(timestamp)
                .logType(logType)
                .source(source).build();

    }

}
