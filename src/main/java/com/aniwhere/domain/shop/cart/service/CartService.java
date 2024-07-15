package com.aniwhere.domain.shop.cart.service;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.mapper.ShopMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final ShopMapper shopMapper;

    public CartService(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    public void addProductToCart(String userId, Integer productId, Integer quantity) {
        if (shopMapper.existsCartItem(userId, productId)) {
            throw new IllegalArgumentException("이미 장바구니에 존재하는 상품입니다."); // 예외 발생

        } else {
            // 새로운 상품 추가
            Cart newCartItem = new Cart();
            newCartItem.setUserId(userId);
            newCartItem.setProductId(productId);
            newCartItem.setQuantity(quantity);
            String productPriceStr = shopMapper.getProductPriceById(productId);
            int productPrice = Integer.parseInt(productPriceStr.replaceAll("[^0-9]", ""));
            int totalPrice = productPrice * quantity;
            newCartItem.setTotalPrice(Integer.parseInt(String.valueOf(totalPrice)));
            shopMapper.insertCartItem(newCartItem);
        }
    }

    public List<Cart> getCartItems(String userId) {
        return shopMapper.getCartItemsByUserId(userId);
    }

    public Integer getTotalOrderPrice(String userId) {
        return shopMapper.getTotalOrderPrice(userId);
    }

    public void updateCartItemQuantity(Integer cartId, Integer quantity, Integer totalPrice) {
        shopMapper.updateCartItemQuantity(cartId, quantity, totalPrice);
    }
    public Cart getCartItemById(Integer cartId) {
        return shopMapper.getCartItemById(cartId);
    }
    public void updateCartItemChecked(Integer cartId, String checked) {
        shopMapper.updateCartItemChecked(cartId, checked);
    }

    public void deleteCartItem(Integer cartId) {
        shopMapper.deleteCartItem(cartId);
    }

}
