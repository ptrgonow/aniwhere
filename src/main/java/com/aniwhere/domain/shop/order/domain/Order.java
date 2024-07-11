package com.aniwhere.domain.shop.order.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Order {
    private String orderId;
    private String orderDateTime;
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
    private String estimatedArrivalDate;
    private String shippingRequest;
    private String productCategory;
    private String productName;
    private String productColor;
    private String productSize;
    private int productQuantity;
    private double productUnitPrice;
    private double productDiscountedPrice;
    private String paymentMethod;
    private String paymentDateTime;
    private double paymentAmount;
    private String bankName;
    private String accountHolder;
    private String accountNumber;
    private String depositedAccount;
    private String invoiceId;
}
