package com.nasa.service.impl;

import com.nasa.bean.PicOfDayBean;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.exception.PicOfDayServiceException;
import com.nasa.mapper.PicOfDayMapper;
import com.nasa.repository.LogEntityRepository;
import com.nasa.repository.PicOfDayRepository;
import com.nasa.service.IPicOfDayService;
import com.nasa.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

import static com.nasa.constants.PicOfDayConstants.*;



@Service
@Qualifier(value = IPicOfDayService.NAME)
public class PicOfDayService implements IPicOfDayService{

	private static final Logger LOGGER = LoggerFactory.getLogger(PicOfDayService.class);

	private final ApplicationEventPublisher publisher;

	PicOfDayMapper mapper = new PicOfDayMapper();
	
	@Autowired
	PicOfDayRepository picOfDayRepository;

	@Autowired
	LogEntityRepository logEntityRepository;

	@Autowired
	RestTemplate client;

    public PicOfDayService(ApplicationEventPublisher publisher, PicOfDayRepository repository, LogEntityRepository logEntityRepository) {
        this.publisher = publisher;
		this.picOfDayRepository = repository;
		this.logEntityRepository = logEntityRepository;
    }

    @Override
	@NonNull
	public PicOfDayEntity fetchTodaysPic() {
		PicOfDayEntity picOfDay = callNasaApi();
		return picOfDayRepository.save(picOfDay);

	}

	private PicOfDayEntity callNasaApi(){

		URI uri = UriComponentsBuilder.fromHttpUrl(NASA_URL)
				.path(PLANETARY_PIC_OF_DAY_API)
				.queryParam("api_key", API_KEY)
				.build().toUri();

		HttpHeaders headers = new HttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(headers);
		PicOfDayEntity result = null;
		try {
			ResponseEntity<PicOfDayBean> response = client.exchange(uri, HttpMethod.GET, entity, PicOfDayBean.class);

			LOGGER.info("Response from Nasa site: {}", response);
			result =Optional.ofNullable(response.getBody())
					.map(mapper::map)
					.orElseThrow(() -> {
						LOGGER.error("NASA returned a null response");
						return new PicOfDayServiceException("Empty Response received.");
					});
		}catch(ResourceAccessException e){
			LOGGER.error("Could not connect to NASA API: [{}]",uri,e);
			throw new PicOfDayServiceException(e);
		}catch (HttpStatusCodeException e){
			LOGGER.error("Received a bad response from NASA: [{}]",uri,e);
			throw new PicOfDayServiceException(e);
		}
		return result;
	}

	@Override
	public PicOfDayEntity getTodaysPic() {
		Date currentDate = DateUtil.getClientDate();
		return picOfDayRepository.getPicOfDayByDate(currentDate);
	}


	@Override
	public PicOfDayEntity getPicByDate(Date date) {
		return picOfDayRepository.getPicOfDayByDate(date);
		
	}

	@Override
	public Slice<PicOfDayEntity> search(String words, int pageNumber){
		Pageable page = PageRequest.of(pageNumber,10);
		return Optional.ofNullable(words)
				.filter(a -> !a.trim().isEmpty())
				.map(a-> picOfDayRepository.search(a,PageRequest.of(pageNumber,10)))
				.orElse(new SliceImpl<>(Collections.emptyList()));
	}
	
	

}
