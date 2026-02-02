package com.nasa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "\"PushSubscription\"", schema = "\"NasaDB\"")
public class PushSubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "SUBSCRIBER")
    private String subscriber;

    @Column(name = "ENDPOINT", nullable = false)
    private String endpoint;

    @Column(name = "PUBLIC_KEY", nullable = false)
    private String publicKey;

    @Column(name = "AUTH_SECRET", nullable = false)
    private String authSecret;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;


    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
    }

}
