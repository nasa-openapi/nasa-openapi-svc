package com.nasa.repository;

import com.nasa.entity.PicOfDayEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface PicOfDayRepository extends JpaRepository<PicOfDayEntity, Integer>{
	
	@Query("SELECT p FROM PicOfDayEntity p WHERE p.publishedDate = :publishedDate")
    PicOfDayEntity getPicOfDayByDate(@Param("publishedDate") Date publishedDate);

    @Query(value = """
            SELECT * from "PicOfDay" WHERE 
            search_vector @@ to_tsquery('english', REPLACE(CAST(plainto_tsquery('english', :keywords) AS TEXT), '&','|')) 
            ORDER BY 
            ts_rank(search_vector, 
            to_tsquery('english', REPLACE(CAST(plainto_tsquery('english', :keywords) AS TEXT), '&','|'))) DESC
            """,
            countQuery = """
                    SELECT count(*) from "PicOfDay" WHERE 
                    search_vector @@ to_tsquery('english', REPLACE(CAST(plainto_tsquery('english,' :keywords) AS TEXT), '&','|'))
                    """,
            nativeQuery = true)
    Slice<PicOfDayEntity> search(@Param("keywords") String keywords, Pageable page);

}
