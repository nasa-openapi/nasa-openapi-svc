package com.nasa.service.impl;

import com.nasa.entity.PicOfDayLogEntity;
import com.nasa.repository.LogEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PicOfDayLogService {

    private final LogEntityRepository logRepository;

    public PicOfDayLogService(LogEntityRepository logRepository) {
        this.logRepository = logRepository;
    }


    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void logSuccess(String source) {
        logRepository.save(buildLogEntity(source,null,"SUCCESS"));
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void logError(String source, String message) {
        logRepository.save(buildLogEntity(source,message,"ERROR"));

    }

    public PicOfDayLogEntity buildLogEntity(String source, String message, String logType){
        return PicOfDayLogEntity.builder()
                .logMessage(message)
                .logType(logType)
                .source(source).build();

    }

}
