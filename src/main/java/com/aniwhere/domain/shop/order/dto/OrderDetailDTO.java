package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {

    private int orderDetailId;
    private String orderId;
    private int productId;
    private int quantity;
    private int price;

}
