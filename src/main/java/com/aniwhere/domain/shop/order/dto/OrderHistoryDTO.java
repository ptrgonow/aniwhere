package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderHistoryDTO {

    private String orderId;
    private String userId;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingAddress3;
    private int totalPrice;
    private String orderStatus;
    private Date orderDate;
    private String recipientName;
    private String recipientEmail;
    private String recipientPhone;
    private String orderRequest;
    private List<OrderDetailDTO> orderItems;
}
