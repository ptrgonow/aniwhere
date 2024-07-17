package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

@Data
public class OrderSearchDTO {

    private String orderId;
    private String userId;
    private int productId;
    private String productName;
    private String image;
    private int totalAmount;
    private int totalQuantity;
    private String orderStatus;
    private String orderDate;
}

