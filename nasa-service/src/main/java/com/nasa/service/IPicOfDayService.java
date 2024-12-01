package com.nasa.service;

import java.util.Date;

import com.nasa.entity.PicOfDayEntity;

public interface IPicOfDayService {
	
	String NAME = "PicOfDayService"; 
	
	PicOfDayEntity fetchTodaysPic();
	
	PicOfDayEntity getTodaysPic();
	
	PicOfDayEntity getPicByDate(Date date);

}
