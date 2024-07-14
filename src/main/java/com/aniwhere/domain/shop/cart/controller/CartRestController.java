package com.aniwhere.domain.shop.cart.controller;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.cart.service.CartService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.mypage.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartRestController {

    private final CartService cartService;
    private final HomeService homeService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addProductToCart(@PathVariable Integer productId,
                                              @RequestParam Integer quantity) {
        String userId = homeService.getAuthenticatedUserId();
        cartService.addProductToCart(userId, productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully.");
    }

    @PostMapping("/update/{cartId}")
    public ResponseEntity<Cart> updateCartItemQuantity(@PathVariable Integer cartId, @RequestParam Integer quantity, @RequestParam Integer totalPrice) {

            cartService.updateCartItemQuantity(cartId, quantity, totalPrice);
            Cart updatedCartItem = cartService.getCartItemById(cartId);
            return ResponseEntity.ok(updatedCartItem);

    }

    @GetMapping("/total-price")
    public ResponseEntity<Integer> getTotalOrderPrice(Authentication authentication) {
        String userId = authentication.getName();
        Integer totalOrderPrice = cartService.getTotalOrderPrice(userId);
        return ResponseEntity.ok(totalOrderPrice);
    }

    @PostMapping("/update/{cartId}/checked")
    public ResponseEntity<?> updateCartItemChecked(@PathVariable Integer cartId, @RequestBody Cart cart) { // @RequestParam 대신 @RequestBody 사용
        try {
            cartService.updateCartItemChecked(cartId, cart.getChecked());
            return ResponseEntity.ok(cartService.getCartItemById(cartId));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update cart item checked status.");
        }
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Integer cartId) {
        try {
            cartService.deleteCartItem(cartId);
            return ResponseEntity.ok("Cart item deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete cart item.");
        }
    }
}
