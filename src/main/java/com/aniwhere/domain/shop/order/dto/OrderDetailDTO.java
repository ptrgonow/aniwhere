package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {

    private int orderDetailId;
    private String orderId;
    private int productId;
    private int quantity;
    private String price;
    private String name;
    private String image;

}