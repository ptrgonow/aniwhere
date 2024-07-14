package com.aniwhere.domain.shop.product.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Product {

    private Integer productId;
    private String name;
    private String image;
    private String price;
    private String detail_url;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
