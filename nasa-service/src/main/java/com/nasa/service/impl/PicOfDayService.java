package com.nasa.service.impl;

import com.nasa.bean.PicOfDayBean;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.exception.PicOfDayServiceException;
import com.nasa.mapper.PicOfDayMapper;
import com.nasa.repository.PicOfDayRepository;
import com.nasa.service.IPicOfDayService;
import com.nasa.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static com.nasa.constants.PicOfDayConstants.*;



@Service
@Qualifier(value = IPicOfDayService.NAME)
public class PicOfDayService implements IPicOfDayService{

	private static final Logger LOGGER = LoggerFactory.getLogger(PicOfDayService.class);

	PicOfDayMapper mapper = new PicOfDayMapper();
	
	@Autowired
	PicOfDayRepository picOfDayRepository;

	@Autowired
	RestTemplate client;

	@Override
	public PicOfDayEntity fetchTodaysPic() {
		StringBuilder url = new StringBuilder();
		url.append(NASA_URL)
			.append(PLANETARY_PIC_OF_DAY_API)
			.append("?")
			.append("api_key=")
			.append(API_KEY);
		
		HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
		PicOfDayEntity result =null;
		try{
			ResponseEntity<PicOfDayBean> response = client.exchange(url.toString(), HttpMethod.GET, entity, PicOfDayBean.class);
			System.out.println();
			System.out.println("Response from Nasa site: " + response.toString());
			PicOfDayEntity picOfDay= mapper.map(response.getBody());
			result  =this.saveTodaysPic(picOfDay);
			System.out.println("Entity id is "+ picOfDay.getId());


		}catch(ResourceAccessException e){
			LOGGER.error("I/O Error in calling NASA API: [{}]",url,e);
			throw new PicOfDayServiceException(e.getMessage());
		}catch (HttpStatusCodeException e){
			LOGGER.error("Error processing request from the NASA Server : [{}]",url,e);
			throw new PicOfDayServiceException(e.getMessage());
		}
		return result;
	}

	@Override
	public PicOfDayEntity getTodaysPic() {
		Date currentDate = DateUtil.getClientDate();
		return picOfDayRepository.getPicOfDayByDate(currentDate);
	}

	
	private PicOfDayEntity saveTodaysPic(PicOfDayEntity entity) {
		return picOfDayRepository.save(entity);
		
	}

	@Override
	public PicOfDayEntity getPicByDate(Date date) {
		return picOfDayRepository.getPicOfDayByDate(date);
		
	}
	
	

}
