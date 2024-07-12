package com.aniwhere.domain.shop.order.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Order {
    private String orderId;
    private LocalDateTime orderDateTime;    // LocalDateTime
    private String orderStatus;
    private BigDecimal totalAmount;
    private int userId;
    private String customerName;
    private String customerId;
    private String customerEmail;
    private String customerPhone;
    private String recipientName;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingAddress3;
    private String shippingCompany;
    private LocalDate estimatedArrivalDate;  // LocalDate 클래스
    private String shippingRequest;
    private String productCategory;
    private String productName;
    private String productColor;
    private String productSize;
    private int productQuantity;
    private BigDecimal productUnitPrice;
    private BigDecimal productDiscountedPrice;
    private String paymentMethod;
    private LocalDateTime paymentDateTime;
    private BigDecimal paymentAmount;
    private String bankName;
    private String accountHolder;
    private String accountNumber;
    private String depositedAccount;
    private String invoiceId;
}
