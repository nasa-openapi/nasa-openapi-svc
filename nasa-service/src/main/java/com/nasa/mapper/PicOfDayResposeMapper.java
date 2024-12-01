package com.nasa.mapper;

import com.nasa.bean.PicOfDayResponseBean;
import com.nasa.entity.PicOfDayEntity;

public class PicOfDayResposeMapper implements Mapper<PicOfDayEntity, PicOfDayResponseBean>{
	
	@Override
	public PicOfDayResponseBean map(PicOfDayEntity entity) {
		return PicOfDayResponseBean.builder()
		.copyright(entity.getCopyright())
		.explanation(entity.getExplanation())
		.url(entity.getUrl()).build();
	}

}
