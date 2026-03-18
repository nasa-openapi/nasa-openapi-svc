package com.nasa.controller;

import com.nasa.bean.ErrorResponseBean;
import com.nasa.bean.PicOfDayResponseBean;
import com.nasa.bean.PicOfDayTaskLogEvent;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.exception.PicOfDayServiceException;
import com.nasa.exception.ResourceNotFoundException;
import com.nasa.mapper.PicOfDayResposeMapper;
import com.nasa.service.IPicOfDayService;
import com.nasa.status.PicOfDayTaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.format.annotation.DateTimeFormat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/nasa/v1/picOfDay")
public class PicOfDayController {

	private final IPicOfDayService picOfDayService;
	
	private final PicOfDayResposeMapper responseMapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(PicOfDayController.class);

	private final ApplicationEventPublisher publisher;

	public PicOfDayController(@Qualifier(IPicOfDayService.NAME) IPicOfDayService picOfDayService,
							  ApplicationEventPublisher publisher){
		this.responseMapper = new PicOfDayResposeMapper();
		this.picOfDayService = picOfDayService;
		this.publisher = publisher;

	}


	@Operation(summary = "Get Pic of Day for a given day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "For a given date param, return pic of day and its details",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PicOfDayResponseBean.class))}),
            @ApiResponse(responseCode = "404", description = "Invalid request",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseBean.class))})
    })
	@GetMapping("/getPicForDay")
	public ResponseEntity<PicOfDayResponseBean> todaysPic(@RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
		return Optional.ofNullable(picOfDayService.getPicByDate(date))
				.map(responseMapper::map)
				.map(bean -> ResponseEntity.status(ACCEPTED)
						.contentType(APPLICATION_JSON).body(bean))
				.orElseThrow(()->new ResourceNotFoundException("Could not find picture for the given day!"));
	}
	
	@Operation(summary = "Save Pic for today")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "This API fetches the current day's pic from NASA"
            		+ " and persists in the database. To be replaced by a cron job later",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PicOfDayResponseBean.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Error",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseBean.class))})
    })
	@GetMapping("/saveTodaysPic")
	public ResponseEntity<PicOfDayResponseBean> saveTodaysPic(){
		PicOfDayEntity entity=null;
		PicOfDayTaskStatus status=null;
		String message=null;
		Instant runAt = Instant.now();
		try{
			entity = picOfDayService.fetchTodaysPic();
			status = PicOfDayTaskStatus.SUCCESS;
			PicOfDayResponseBean responseBean = responseMapper.map(entity);
			return ResponseEntity.status(CREATED)
					.contentType(APPLICATION_JSON)
					.body(responseBean);
		}catch(Exception e){
			status = PicOfDayTaskStatus.ERROR;
			message = e.getMessage();
			if(!(e instanceof PicOfDayServiceException)){
				LOGGER.error("FATAL ERROR: UNKNOWN ERROR -- ",e);
			}
			throw e;
		}finally {
			Integer entityID = entity ==null? null: entity.getId();
			publisher.publishEvent(new PicOfDayTaskLogEvent("API",message, entityID, runAt, status));
		}

	}

	@Operation(summary = "Get today's Astronomy Picture of the Day")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved today's picture",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = PicOfDayResponseBean.class)
					)),
			@ApiResponse(responseCode = "404", description = "Picture for today not found",
					content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseBean.class))})
	})
	@GetMapping("/getTodaysPic")
	public ResponseEntity<PicOfDayResponseBean> getTodaysPic(){
		return Optional.ofNullable(picOfDayService.getTodaysPic())
				.map(responseMapper::map)
				.map((bean) ->
						ResponseEntity.status(ACCEPTED).contentType(APPLICATION_JSON).body(bean))
				.orElseThrow(()-> new PicOfDayServiceException("Could not find picture for today!"));
	}


}
