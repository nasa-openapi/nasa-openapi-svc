package com.nasa.service;

import java.util.Date;

import com.nasa.entity.PicOfDayEntity;

public interface IPicOfDayService {
	
	String NAME = "PicOfDayService";

	/**
	 * Fetch today's pic from NASA via REST API
	 * @return saved entity for the picofday
	 */
	PicOfDayEntity fetchTodaysPic();

	/**
	 * Get today's pic from local db
	 * @return today's picture
	 */
	PicOfDayEntity getTodaysPic();

	/**
	 * get picture for  certain date param
	 * @param date
	 * @return picture for the date param
	 */
	PicOfDayEntity getPicByDate(Date date);

}
