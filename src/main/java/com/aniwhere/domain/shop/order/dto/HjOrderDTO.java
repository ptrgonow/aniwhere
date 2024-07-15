package com.aniwhere.domain.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HjOrderDTO {
    private String order_id;
    private String user_id;
    private String shipping_address1;
    private String shipping_address2;
    private String shipping_address3;
    private Double amount;
    private String order_status;
    private Timestamp order_date;
    private String recipient_name;
    private String recipient_email;
    private String recipient_phone;
    private String order_request;
    private String product_image;
    private int quantity;
    private Double price;
    private String product_name;
    private String product_id;

    // TODO : 의도 물어보기
    public enum OrderStatus {
        결제_전,
        결제_완료,
        결제_취소
    }
}
