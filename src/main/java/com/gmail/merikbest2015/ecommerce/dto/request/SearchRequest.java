package com.gmail.merikbest2015.ecommerce.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private List<String> perfumers;
    private List<String> genders;
    private Integer price = 0;
    private String searchType;
    private String text;



    private List<String> subCategories;
    private Integer priceStart;
    private Integer priceEnd;
    private Boolean popular;
//    private String searchType;
//    private String text;
}
