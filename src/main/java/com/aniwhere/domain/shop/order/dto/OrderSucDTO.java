package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderSucDTO {

    private String orderId;
    private String userId;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingAddress3;
    private int amount;
    private int productId;
    private int price;
    private int quantity;
    private String name;
    private int isSocial;
    private String userName;
    private Timestamp orderDate;
    private String orderStatus;
    private String recipientName;
    private String recipientEmail;
    private String recipientPhone;
    private String orderRequest;
}
