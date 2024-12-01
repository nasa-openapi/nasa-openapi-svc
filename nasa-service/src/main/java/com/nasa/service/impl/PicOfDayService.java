package com.nasa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nasa.bean.PicOfDayBean;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.mapper.PicOfDayMapper;
import com.nasa.repository.PicOfDayRepository;
import com.nasa.service.IPicOfDayService;
import com.nasa.util.DateUtil;

import static com.nasa.constants.PicOfDayConstants.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
@Qualifier(value = IPicOfDayService.NAME)
public class PicOfDayService implements IPicOfDayService{
	
	RestTemplate client = new RestTemplate();
	PicOfDayMapper mapper = new PicOfDayMapper();
	
	@Autowired
	PicOfDayRepository picOfDayRepository;

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

        ResponseEntity<PicOfDayBean> response = client.exchange(url.toString(), HttpMethod.GET, entity, PicOfDayBean.class);
        System.out.println();
        System.out.println("Response from Nasa site: " + response.toString());
        PicOfDayEntity picOfDay= mapper.map(response.getBody());
        picOfDay =this.saveTodaysPic(picOfDay);
        
        System.out.println("Entity id is "+ picOfDay.getId());
        return picOfDay;
		
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
