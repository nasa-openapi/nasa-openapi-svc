package com.nasa.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchResponse {

    private List<PicOfDayResponseBean> items;
    private Boolean hasMore;
    private Integer currentPage;

}
