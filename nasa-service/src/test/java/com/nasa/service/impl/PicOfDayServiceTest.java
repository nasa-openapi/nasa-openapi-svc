package com.nasa.service.impl;
import com.nasa.bean.PicOfDayBean;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.exception.PicOfDayServiceException;
import com.nasa.mapper.PicOfDayMapper;
import com.nasa.repository.PicOfDayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PicOfDayServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PicOfDayRepository repository;

    @InjectMocks
    private PicOfDayService service;

    private PicOfDayBean bean;
    private PicOfDayEntity entity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        bean = new PicOfDayBean();
        bean.setTitle("Galaxy");
        bean.setUrl("https://nasa.gov/galaxy");
        bean.setDate("2025-11-03");
        bean.setExplanation("Beautiful galaxy.");
        bean.setMediaType("image");

        entity = new PicOfDayMapper().map(bean);
    }

    @Test
    void fetchTodaysPic_success() {
        ResponseEntity<PicOfDayBean> response = new ResponseEntity<>(bean, HttpStatus.OK);

        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(PicOfDayBean.class)))
                .thenReturn(response);
        when(repository.save(any(PicOfDayEntity.class))).thenReturn(entity);

        PicOfDayEntity result = service.fetchTodaysPic();

        assertNotNull(result);
        assertEquals("Galaxy", result.getTitle());
        verify(repository, times(1)).save(any(PicOfDayEntity.class));
    }

    @Test
    void fetchTodaysPic_nullBody_throwsException() {
        ResponseEntity<PicOfDayBean> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(PicOfDayBean.class)))
                .thenReturn(response);

        PicOfDayServiceException ex = assertThrows(PicOfDayServiceException.class, () -> service.fetchTodaysPic());
        assertEquals("Empty Response received.", ex.getMessage());
    }

    @Test
    void fetchTodaysPic_ioError_throwsException() {
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(PicOfDayBean.class)))
                .thenThrow(new ResourceAccessException("Connection timed out"));

        assertThrows(PicOfDayServiceException.class, () -> service.fetchTodaysPic());
    }

    @Test
    void fetchTodaysPic_httpError_throwsException() {
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(PicOfDayBean.class)))
                .thenThrow(mock(HttpStatusCodeException.class));

        assertThrows(PicOfDayServiceException.class, () -> service.fetchTodaysPic());
    }
}
