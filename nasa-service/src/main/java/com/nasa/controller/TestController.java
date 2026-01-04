package com.nasa.controller;


import com.nasa.bean.PushSubscriptionBean;
import com.nasa.entity.PushSubscriptionEntity;
import com.nasa.repository.PushSubscriptionRepository;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/nasa/v1/test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Autowired
    PushSubscriptionRepository pushSubscriptionRepository;

    @Autowired
    PushService pushService;

    @GetMapping("sendNotification")
    public void sendNotification() throws GeneralSecurityException, JoseException, IOException, ExecutionException, InterruptedException {

        List<PushSubscriptionEntity> subscriptions=new ArrayList<>();
        try{
            subscriptions = pushSubscriptionRepository
                    .findAll();
        }catch(DataAccessException e){
            LOGGER.error("ERROR fetching subscription details: ", e);
            return;
        }
        for (PushSubscriptionEntity entity : subscriptions) {
            try {


                String payload = "Check out Astronomy Picture of the Day!!";
                Notification notification = new Notification(
                        entity.getEndpoint(),
                        entity.getPublicKey(),
                        entity.getAuthSecret(),
                        payload.getBytes(StandardCharsets.UTF_8)
                );

                HttpResponse response = pushService.send(notification);
                int status = response.getStatusLine().getStatusCode();
                String fcmId = response.getFirstHeader("Location")!=null?
                        response.getFirstHeader("Location").getValue():"N/A";
                if (status == 200|| status == 201) {
                    LOGGER.debug("Push success Subscriber: {} with status: {}",entity.getSubscriber(), status);
                } else {
                    LOGGER.error("Failed to send push to subscriber: {}, TraceID: {}, endpoint: {},  Status: {}, Reason: {}",
                            entity.getSubscriber(),
                            fcmId,
                            entity.getEndpoint(),
                            response.getStatusLine().getStatusCode(),
                            response.getStatusLine().getReasonPhrase());
                }


            } catch (GeneralSecurityException | IOException | JoseException | ExecutionException |
                     InterruptedException e) {
                String id = entity.getSubscriber()!=null?entity.getSubscriber():entity.getId().toString();
                LOGGER.error("PUSH FAILED FOR {}:",id, e);

            }
        }
    }


    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody PushSubscriptionBean dto) {

        if (dto.getEndpoint() == null || dto.getKeys() == null
                || dto.getKeys().getP256dh() == null || dto.getKeys().getAuth() == null) {
            return ResponseEntity.badRequest().body("Invalid subscription payload");
        }

        PushSubscriptionEntity entity = new PushSubscriptionEntity();
        entity.setEndpoint(dto.getEndpoint());
        entity.setPublicKey(dto.getKeys().getP256dh());
        entity.setAuthSecret(dto.getKeys().getAuth());
        LOGGER.info("entity to be saved {}", entity);
        pushSubscriptionRepository.save(entity);

        return ResponseEntity.status(201).body("Subscription saved");
    }




}
