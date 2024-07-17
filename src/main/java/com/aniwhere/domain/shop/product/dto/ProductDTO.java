package com.aniwhere.domain.shop.product.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class ProductDTO {

    private Integer productId;
    private String name;
    private String image;
    private String price;
    private String detail_url;
    private String category;
    private Date createdAt;
    private Date updatedAt;
    private int quantity;
}
