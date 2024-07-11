package com.aniwhere.domain.shop.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class OrderDTO {
    private String orderId;
    private LocalDateTime orderDateTime;
    private String orderStatus;
    private double totalAmount;
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
    private double productUnitPrice;
    private double productDiscountedPrice;
    private String paymentMethod;
    private LocalDateTime paymentDateTime;
    private double paymentAmount;
    private String bankName;
    private String accountHolder;
    private String accountNumber;
    private String depositedAccount;
    private String invoiceId;
}
