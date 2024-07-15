package com.aniwhere.domain.shop.cart.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CartDTO {

    private int cartId;
    private int totalPrice;
    private String name;
    private String image;
    private String price;
    private String checked;
    private String userId;
    private int productId;
    private int quantity;
    private Date createdAt;
    private Date updatedAt;

}
