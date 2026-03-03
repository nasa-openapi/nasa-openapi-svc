package com.nasa.bean;

import com.nasa.status.PicOfDayTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PicOfDayTaskLogEvent {

    private String source;

    private String message;

    private Integer entityID;

    private Instant runAt;

    private  PicOfDayTaskStatus status;

}
