package com.aniwhere.domain.shop.product.dto;

import com.aniwhere.domain.shop.product.domain.Product;
import lombok.Data;

import java.util.List;

@Data
public class PageDTO {
    private List<Product> products;
    private int totalPages;
    private int currentPage;
    private int pageSize;

}
