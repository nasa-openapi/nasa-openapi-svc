package com.nasa.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "\"PicOfDay\"",  schema = "\"NasaDB\"")
public class PicOfDayEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Integer id;
	
	@Column(name = "COPYRIGHT", nullable = true)
    private String copyright;

	@Column(name = "PUBLISHED_DATE", nullable = false)
    private Date publishedDate;

	@Column(name = "EXPLANATION", length = 2048, nullable = true)
    private String explanation;
	
	@Column(name = "MEDIATYPE", length = 32, nullable = false)
    private String mediaType;

	@Column(name = "TITLE", length = 128, nullable = false)
    private String title;

	@Column(name = "URL", length = 128, nullable = false)
    private String url;
	
	
	
}
