package com.aniwhere.domain.shop.product.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Integer productId;
    private String name;
    private String image;
    private String price;
    private String detail_url;
    private String category;
}
