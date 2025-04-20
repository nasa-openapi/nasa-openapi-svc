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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/nasa/v1/picOfDay")
public class PicOfDayController {
	
	RestTemplate client = new RestTemplate();
	
	@Autowired
	@Qualifier(value = IPicOfDayService.NAME)
	IPicOfDayService picOfDayService;
	
	PicOfDayResposeMapper responseMapper = new PicOfDayResposeMapper();
	
	@Operation(summary = "Get Pic of Day for a givem day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "For a given date param, return pic of day and its details",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PicOfDayEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
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
	
	@Operation(summary = "Save Pic for today")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "This API fetches the current day's pic from NASA"
            		+ " and persists in the database. To be replaced by a cron job later",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PicOfDayEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
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
