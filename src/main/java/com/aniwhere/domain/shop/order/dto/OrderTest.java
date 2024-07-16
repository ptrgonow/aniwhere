package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

@Data
public class OrderTest {
    private String orderId;
    private String userId;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingAddress3;
    private int amount;
    private String orderStatus;
    private String recipientName;
    private String recipientEmail;
    private String recipientPhone;
    private String orderRequest;
}
