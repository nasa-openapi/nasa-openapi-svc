package com.nasa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "\"PicOfDayLog\"",  schema = "\"NasaDB\"")
public class PicOfDayLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "RUN_TIMESTAMP", nullable = false)
    private Instant runTime = Instant.now();

    @Column(name = "LOG_MESSAGE", length = 2048, nullable = false)
    private String logMessage;

    @Column(name = "LOG_TYPE", nullable = false)
    private String logType;

    @Column(name = "SOURCE")
    private String source;




}

