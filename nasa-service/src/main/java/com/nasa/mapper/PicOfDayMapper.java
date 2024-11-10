package com.nasa.mapper;

import java.util.Date;

import com.nasa.bean.PicOfDayBean;
import com.nasa.entity.PicOfDayEntity;
import com.nasa.util.DateUtil;

public class PicOfDayMapper implements Mapper<PicOfDayBean, PicOfDayEntity>{

	@Override
	public PicOfDayEntity map(PicOfDayBean dto) {
		Date pubDate = DateUtil.convertDate(dto.getDate());
		PicOfDayEntity result =  PicOfDayEntity.builder()
			.copyright(dto.getCopyright())
			.explanation(dto.getExplanation())
			.mediaType(dto.getMediaType())
			.publishedDate(pubDate)
			.title(dto.getTitle())
			.url(dto.getUrl()).build();
		System.out.println("Entity created: "+ result.toString());
		return result;
	}

}
