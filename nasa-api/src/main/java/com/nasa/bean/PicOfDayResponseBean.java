package com.nasa.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PicOfDayResponseBean {
	
	private String copyright;
	
	private String explanation;
	
	private String title;
	
	private String url;

}
