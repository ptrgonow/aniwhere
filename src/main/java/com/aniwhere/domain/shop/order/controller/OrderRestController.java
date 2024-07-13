package com.aniwhere.domain.shop.order.controller;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principal;
                return  oauthUser.getAttribute("userId");
            }
        }
        return null;
    }
/*
    @GetMapping
    public List<OrderDTO> getOrdersByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        return orderService.findOrdersByDateRange(startDate, endDate);
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable String orderId) {
        return orderService.findOrderById(orderId);
    }
*/
    @GetMapping("/items") // 엔드포인트 변경
    public ResponseEntity<Map<String, Object>> getCheckedItems(Authentication authentication) {
        String userId = getAuthenticatedUserId();
        List<Cart> checkedItems = orderService.getCheckedCartItems(userId);
        int totalProductPrice = checkedItems.stream()
                .mapToInt(Cart::getTotalPrice)
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("orderItems", checkedItems); // orderItems로 변경
        response.put("totalProductPrice", totalProductPrice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success/payment")
    public String handlePaymentSuccess(@RequestParam("orderId") String orderId,
                                       @RequestParam("customerEmail") String customerEmail,
                                       @RequestParam("customerName") String customerName,
                                       @RequestParam("customerMobilePhone") String customerMobilePhone,
                                       @RequestParam("totalPrice") int totalPrice) {

        String userId = getAuthenticatedUserId();
        orderService.saveOrder(orderId, userId, customerEmail, customerName, customerMobilePhone, totalPrice);

        return "redirect:/shop/cart";
    }
}