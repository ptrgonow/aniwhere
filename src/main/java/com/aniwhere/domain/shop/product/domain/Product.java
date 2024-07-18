package com.aniwhere.domain.shop.product.domain;

import com.aniwhere.domain.shop.product.dto.ProductDTO;
import com.aniwhere.domain.shop.review.domain.Review;
import com.aniwhere.domain.shop.review.dto.ReviewDTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Product {

    private Integer productId;
    private String name;
    private String image;
    private String price;
    private int quantity;
    private String detailUrl;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Product fromDTO(ProductDTO dto) {
        Product product= new Product();

        product.setName(dto.getName());
        product.setImage(dto.getImage());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setDetailUrl(dto.getDetailUrl());
        product.setCategory(dto.getCategory());

        return product;
    }
}
