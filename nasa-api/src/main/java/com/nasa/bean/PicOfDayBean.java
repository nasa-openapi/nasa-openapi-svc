package com.nasa.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PicOfDayBean {

	@JsonProperty("copyright")
    private String copyright;

	@JsonProperty("date")
    private String date;

	@JsonProperty("explanation")
    private String explanation;

	@JsonProperty("hdurl")
    private String hdurl;

	
    @JsonProperty("media_type")
    private String mediaType;

    @JsonProperty("service_version")
    private String serviceVersion;

    @JsonProperty("title")
    private String title;

    @JsonProperty("url")
    private String url;
}

