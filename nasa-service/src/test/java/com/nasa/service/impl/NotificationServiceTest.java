package com.nasa.service.impl;

import com.nasa.entity.PushSubscriptionEntity;
import com.nasa.repository.PushSubscriptionRepository;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.io.IOException;
import java.security.Security;
import java.util.List;

import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private PushSubscriptionRepository repository;

    @Mock
    private PushService pushService;

    @InjectMocks
    private NotificationService notificationService;

    private static final String PUBLIC_KEY = "BPbBYcMcV7YY5dGNTUjkzUAxXuCDfLloTl4vCQ9xGzgxKlLVCig8-zHnUY-z-_KjLJWbhQBQdfFP-d3UcIkFcT0";

    private static final String AUTH = "X22hmSkQ1GFXdGzglPFdQQ";    //ggignore

    private static final String ENDPOINT = "https://fcm.googleapis.com/fcm/send/sample-endpoint";

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        if(Security.getProvider("BC")== null)
            Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    @DisplayName("Should send notifications successfully to all subscribers")
    void testSendNotification_Success() throws Exception {
        PushSubscriptionEntity sub = PushSubscriptionEntity.builder().
            subscriber("user1").
            endpoint(ENDPOINT).
            publicKey(PUBLIC_KEY).
            authSecret(AUTH).build();

        when(repository.findAll()).thenReturn(List.of(sub));

        // Mocking the HttpResponse from Apache HttpClient
        HttpResponse mockResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(201);
        when(mockResponse.getStatusLine()).thenReturn(statusLine);

        when(pushService.send(any(Notification.class))).thenReturn(mockResponse);

        // Act
        notificationService.sendNotification();

        // Assert
        verify(pushService, times(1)).send(any(Notification.class));
    }

    @Test
    @DisplayName("Should log error but continue when database fetch fails")
    void testSendNotification_DatabaseError() {
        // Arrange
        when(repository.findAll()).thenThrow(new DataAccessException("DB Down") {});

        // Act
        notificationService.sendNotification();

        // Assert
        verifyNoInteractions(pushService);
    }

    @Test
    @DisplayName("Error when trying to send notification")
    void testSendNotification_PushError() throws Exception{

        PushSubscriptionEntity sub1 = PushSubscriptionEntity.builder().
                subscriber("user1").
                endpoint(ENDPOINT).
                publicKey(PUBLIC_KEY).
                authSecret(AUTH).build();

        PushSubscriptionEntity sub2 = PushSubscriptionEntity.builder().
                subscriber("user2").
                endpoint(ENDPOINT).
                publicKey(PUBLIC_KEY).
                authSecret(AUTH).build();
        // Arrange
        when(repository.findAll()).thenReturn(List.of(sub1, sub2));

        when(pushService.send(any(Notification.class))).thenThrow(new IOException("something happened"));

        // Act
        notificationService.sendNotification();

        // Assert
        verify(pushService, times(2)).send(any(Notification.class));
    }
}
