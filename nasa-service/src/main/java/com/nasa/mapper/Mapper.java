package com.nasa.mapper;

public interface Mapper<T,R> {
	
	R map(T dto);

}
