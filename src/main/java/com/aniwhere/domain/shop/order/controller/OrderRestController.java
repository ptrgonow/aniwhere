package com.aniwhere.domain.shop.order.controller;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.dto.OrderDTO;
import com.aniwhere.domain.shop.order.dto.OrderHistoryDTO;
import com.aniwhere.domain.shop.order.service.OrderService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class OrderRestController {

    private final OrderService orderService;
    private final HomeService homeService;

/*    @GetMapping
    public List<OrderDTO> getOrdersByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        return orderService.findOrdersByDateRange(startDate, endDate);
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable String orderId) {
        return orderService.findOrderById(orderId);
    }*/

    @GetMapping("/items") // 엔드포인트 변경
    public ResponseEntity<Map<String, Object>> getCheckedItems() {
        String userId = homeService.getAuthenticatedUserId();
        List<Cart> checkedItems = orderService.getCheckedCartItems(userId);
        int totalProductPrice = checkedItems.stream()
                .mapToInt(Cart::getTotalPrice)
                .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("orderItems", checkedItems); // orderItems로 변경
        response.put("totalProductPrice", totalProductPrice);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/success")
    public ResponseEntity<String> saveOrder(@RequestBody OrderHistoryDTO orderHistory) {
        try {
            orderService.saveOrder(
                    orderHistory.getOrderDTO(),
                    orderHistory.getOrderItems()
            );
            return ResponseEntity.ok("주문 정보 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("주문 정보 저장 실패: " + e.getMessage());
        }
    }

}

