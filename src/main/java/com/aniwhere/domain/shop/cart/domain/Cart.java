package com.aniwhere.domain.shop.cart.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Cart {

    private int cartId;
    private String name;
    private int totalPrice;
    private String checked;
    private String image;
    private String price;
    private String userId;
    private int productId;
    private int quantity;
    private Date createdAt;
    private Date updatedAt;
}
