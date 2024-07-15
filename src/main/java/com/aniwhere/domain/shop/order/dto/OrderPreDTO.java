package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

@Data
public class OrderPreDTO {

    private String orderId;
    private int amount;
    private String userId;

}
