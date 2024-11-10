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
import com.nasa.entity.PicOfDayEntity;
import com.nasa.service.IPicOfDayService;

@RestController
@RequestMapping("/nasa/v1/picOfDay")
public class PicOfDayController {
	
	RestTemplate client = new RestTemplate();
	
	@Autowired
	@Qualifier(value = IPicOfDayService.NAME)
	IPicOfDayService picOfDayService;
	
	
	@GetMapping("/getPicForDay")
	public ResponseEntity<PicOfDayEntity> todaysPic(@RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
		PicOfDayEntity entity =  picOfDayService.getPicByDate(date);
		return ResponseEntity.status(HttpStatus.ACCEPTED)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(entity);
	}
	
	@GetMapping("/saveTodaysPic")
	public ResponseEntity<PicOfDayEntity> saveTodaysPic(){
		PicOfDayEntity entity =picOfDayService.fetchTodaysPic();
		return ResponseEntity.status(HttpStatus.CREATED)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(entity);
			
	}
	
	@GetMapping("/getTodaysPic")
	public ResponseEntity<PicOfDayEntity> getTodaysPic(){
		PicOfDayEntity entity = picOfDayService.getTodaysPic();
		return ResponseEntity.status(HttpStatus.ACCEPTED)
        		.contentType(MediaType.APPLICATION_JSON)
        		.body(entity);
	}


}
