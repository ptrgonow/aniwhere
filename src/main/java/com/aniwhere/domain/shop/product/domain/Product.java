package com.aniwhere.domain.shop.product.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Product {

    private Integer productId;
    private String name;
    private String link;
    private String image;
    private String price;
    private String hprice;
    private String brand;
    private String maker;
    private String detail_url;
    private String category1;
    private String category2;
    private String category3;
    private String category4;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String naverProductId;
}
