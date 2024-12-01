package com.nasa.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nasa.entity.PicOfDayEntity;

public interface PicOfDayRepository extends JpaRepository<PicOfDayEntity, Integer>{
	
	@Query("SELECT p FROM PicOfDayEntity p WHERE p.publishedDate = :publishedDate")
    PicOfDayEntity getPicOfDayByDate(@Param("publishedDate") Date publishedDate);

}
