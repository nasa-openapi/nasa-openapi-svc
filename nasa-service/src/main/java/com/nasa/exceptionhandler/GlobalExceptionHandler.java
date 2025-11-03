package com.nasa.exceptionhandler;

import com.nasa.bean.ErrorResponseBean;
import com.nasa.exception.PicOfDayServiceException;
import org.springframework.dao.DataAccessException;
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
        return getErrorResponseBeanResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseBean> handleIllegalArgumentException(IllegalArgumentException ex) {
        return getErrorResponseBeanResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PicOfDayServiceException.class)
    public ResponseEntity<ErrorResponseBean> handlePicOfDayServiceException(PicOfDayServiceException ex){
        return getErrorResponseBeanResponseEntity("Currently unavailable. Please Try another time.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseBean> handleDataAccessException(DataAccessException ex) {
        return getErrorResponseBeanResponseEntity("There was an unexpected error connecting to the database.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<ErrorResponseBean> getErrorResponseBeanResponseEntity(String message, HttpStatus status) {
        ErrorResponseBean response = ErrorResponseBean.builder()
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .build();

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}