package com.aniwhere.domain.shop.order.controller;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.dto.OrderSearchDTO;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.shop.order.dto.OrderHistoryDTO;
import com.aniwhere.domain.shop.order.service.OrderService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/order")
    public ResponseEntity<String> saveOrderBeforeCheckOut(@RequestBody OrderHistoryDTO orderHistory) {
        try {
            orderService.saveOrder(
                    orderHistory.getOrderPreDTO(),
                    orderHistory.getOrderItems()
            );
            return ResponseEntity.ok("주문 정보 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("주문 정보 저장 실패: " + e.getMessage());
        }
    }
    @PostMapping("/success")
    public ResponseEntity<String> saveOrder(@RequestBody OrderSucDTO orderSucDTO) {
        try {
            String userId = homeService.getAuthenticatedUserId();
            orderSucDTO.setUserId(userId);

            orderService.makeOrder(orderSucDTO);

            return ResponseEntity.ok("주문 정보 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("주문 정보 저장 실패: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderSearchDTO>> getOrderItemsById(@RequestParam String userId, @RequestParam String startDate, @RequestParam String endDate) {
        List<OrderSearchDTO> searchItems = orderService.getOrderItemsByUserIdForDate(userId, startDate, endDate);
        return ResponseEntity.ok(searchItems);
    }

}

