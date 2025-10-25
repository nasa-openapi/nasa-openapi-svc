package com.nasa.exceptionhandler;

import com.nasa.bean.ErrorResponseBean;
import com.nasa.exception.PicOfDayServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nasa.exception.ResourceNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseBean> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponseBean responseBean = ErrorResponseBean.builder().message(ex.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.NOT_FOUND.value()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).
                body(responseBean);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseBean> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponseBean responseBean = ErrorResponseBean.builder().message(ex.getMessage())
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.BAD_REQUEST.value()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).
                body(responseBean);
    }

    @ExceptionHandler(PicOfDayServiceException.class)
    public ResponseEntity<ErrorResponseBean> handlePicOfDayServiceException(PicOfDayServiceException ex){
        ErrorResponseBean responseBean = ErrorResponseBean.builder().message("Currently unavailable. Please Try another time.")
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.SERVICE_UNAVAILABLE.value()).build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).contentType(MediaType.APPLICATION_JSON).
                body(responseBean);
    }
}