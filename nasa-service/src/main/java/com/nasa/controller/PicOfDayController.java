package com.nasa.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.nasa.bean.PicOfDayBean;
import com.nasa.bean.PicOfDayResponseBean;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.exception.ResourceNotFoundException;
import com.nasa.mapper.PicOfDayResposeMapper;
import com.nasa.service.IPicOfDayService;

@RestController
@RequestMapping("/nasa/v1/picOfDay")
public class PicOfDayController {
	
	RestTemplate client = new RestTemplate();
	
	@Autowired
	@Qualifier(value = IPicOfDayService.NAME)
	IPicOfDayService picOfDayService;
	
	PicOfDayResposeMapper responseMapper = new PicOfDayResposeMapper();
	
	
	@GetMapping("/getPicForDay")
	public ResponseEntity<PicOfDayResponseBean> todaysPic(@RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
		PicOfDayEntity entity =  picOfDayService.getPicByDate(date);
		if(entity == null) {
			throw new ResourceNotFoundException("Could not find picture for the given day!");
		}
		PicOfDayResponseBean responseBean = responseMapper.map(entity);
		return ResponseEntity.status(HttpStatus.ACCEPTED)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(responseBean);
	}
	
	@GetMapping("/saveTodaysPic")
	public ResponseEntity<PicOfDayResponseBean> saveTodaysPic(){
		PicOfDayEntity entity =picOfDayService.fetchTodaysPic();
		PicOfDayResponseBean responseBean = responseMapper.map(entity);
		return ResponseEntity.status(HttpStatus.CREATED)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(responseBean);
			
	}
	
	@GetMapping("/getTodaysPic")
	public ResponseEntity<PicOfDayResponseBean> getTodaysPic(){
		PicOfDayEntity entity = picOfDayService.getTodaysPic();
		if(entity == null) {
			throw new ResourceNotFoundException("Could not find picture for today!");
		}
		PicOfDayResponseBean responseBean = responseMapper.map(entity);
		return ResponseEntity.status(HttpStatus.ACCEPTED)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(responseBean);
	}


}
