package com.nasa.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorResponseBean {

    private String message;
    private int status;
    private String timestamp;
}
