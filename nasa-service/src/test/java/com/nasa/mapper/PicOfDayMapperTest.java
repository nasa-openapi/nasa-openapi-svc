package com.nasa.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.nasa.bean.PicOfDayBean;
import com.nasa.entity.PicOfDayEntity;

//@SpringBootTest
//@ActiveProfiles("test")
public class PicOfDayMapperTest {
	
	PicOfDayMapper cut = new PicOfDayMapper();
	
	
	@Test
	public void testMapSuccessfull() {
		PicOfDayBean bean = new PicOfDayBean();
		bean.setDate("2024-01-01");
		bean.setCopyright("copyright");
		bean.setUrl("url");
		bean.setTitle("title");
		bean.setExplanation("explanation");
		bean.setMediaType("jpeg");
		PicOfDayEntity entity = cut.map(bean);
		Assertions.assertEquals("copyright", entity.getCopyright());
	}
	
	@Test
	public void testMapFailForDate() {
		PicOfDayBean bean = new PicOfDayBean();
		bean.setDate("11-1-2023");
		bean.setCopyright("copyright");
		bean.setUrl("url");
		bean.setTitle("title");
		bean.setExplanation("explanation");
		bean.setMediaType("jpeg");
		Assertions.assertThrows(IllegalArgumentException.class, 
				() -> cut.map(bean));
	}
	
	
	
	


}
