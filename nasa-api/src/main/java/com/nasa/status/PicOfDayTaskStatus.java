package com.nasa.status;

public enum PicOfDayTaskStatus {
    SUCCESS("SUCCESS"),
    ERROR("ERROR");

    private final String status;

    PicOfDayTaskStatus(String status){
        this.status = status;
    }
}
