package com.nasa.builder;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nasa.entity.PicOfDayEntity;

public class PicOfDayBuilder {
	
	
    private String copyright;

	
    private Date publishedDate;

	
    private String explanation;


    private String mediaType;


    private String title;

    private String url;
    
    public PicOfDayBuilder setCopyRight(String a) {
    	this.copyright = a;
    	return this;
    }
    
    public PicOfDayBuilder date(Date a) {
    	this.publishedDate = a;
    	return this;
    }
    
    public PicOfDayBuilder setExplanation(String a) {
    	this.explanation = a;
    	return this;
    }
    
    public PicOfDayBuilder setMediaType(String a) {
    	this.mediaType = a;
    	return this;
    }
    
    public PicOfDayBuilder setTitle(String a) {
    	this.title = a;
    	return this;
    }
    
    public PicOfDayBuilder setUrl(String a) {
    	this.url = a;
    	return this;
    }
    
    
    public PicOfDayEntity build() {
    	return new PicOfDayEntity(null, copyright,publishedDate,
    			explanation, mediaType, title, url);
    }

}
