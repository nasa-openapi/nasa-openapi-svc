package com.nasa.notification;


import com.nasa.entity.PicOfDayEntity;
import com.nasa.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Subscriber for event save today's pic
 * from @{{@link com.nasa.service.IPicOfDayService}}
 */
@Component
public class NotificationSubscriber {

    @Autowired
    private INotificationService notificationService;


    @EventListener
    @Async
    public void subscribe(PicOfDayEntity entity){
        String caption = entity.getTitle()==null? "Gaze up the stars!": entity.getTitle();
        notificationService.sendNotification(caption);

    }
}
