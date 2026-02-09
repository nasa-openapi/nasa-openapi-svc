package com.nasa.controller;


import com.nasa.bean.PushSubscriptionBean;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.entity.PushSubscriptionEntity;
import com.nasa.mapper.PushSubscriptionEntityMapper;
import com.nasa.repository.PushSubscriptionRepository;
import com.nasa.service.INotificationService;
import com.nasa.service.IPicOfDayService;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/nasa/v1/test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    PushSubscriptionRepository pushSubscriptionRepository;

    @Autowired
    PushService pushService;

    @Autowired
    @Qualifier(IPicOfDayService.NAME)
    IPicOfDayService picOfDayService;

    @Autowired
    @Qualifier("notificationThread")
    Executor notificationExecutor;

    @Autowired
    INotificationService notificationService;

    @GetMapping("sendNotification")
    public void sendNotification() throws GeneralSecurityException, JoseException, IOException, ExecutionException, InterruptedException {
        PicOfDayEntity today = picOfDayService.getTodaysPic();
        String caption = today.getTitle()==null? "Gaze up the stars!": today.getTitle();
        notificationExecutor.execute(()->{
            try{
                notificationService.sendNotification(caption);
            }catch(Exception e){
                LOGGER.error("Error while sending notifications");
            }
        });

    }


    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody PushSubscriptionBean dto) {

        if (dto.getEndpoint() == null || dto.getKeys() == null
                || dto.getKeys().getP256dh() == null || dto.getKeys().getAuth() == null) {
            return ResponseEntity.badRequest().body("Invalid subscription payload");
        }

        PushSubscriptionEntity entity = new PushSubscriptionEntityMapper().map(dto);

        LOGGER.info("entity to be saved {}", entity);
        pushSubscriptionRepository.save(entity);

        return ResponseEntity.status(201).body("Subscription saved");
    }




}
