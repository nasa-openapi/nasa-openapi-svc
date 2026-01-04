package com.nasa.service.impl;

import com.nasa.entity.PushSubscriptionEntity;
import com.nasa.repository.PushSubscriptionRepository;
import com.nasa.service.INotificationService;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationService implements INotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final PushSubscriptionRepository pushSubscriptionRepository;
    private final PushService pushService;

    public NotificationService(PushSubscriptionRepository pushSubscriptionRepository, PushService pushService) {
        this.pushSubscriptionRepository = pushSubscriptionRepository;
        this.pushService = pushService;
    }

    @Override
    public void sendNotification() {
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

}

