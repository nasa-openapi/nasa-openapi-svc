package com.nasa.repository;

import com.nasa.entity.PicOfDayLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntityRepository extends JpaRepository<PicOfDayLogEntity, Integer> {
}
