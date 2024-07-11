package com.aniwhere.domain.shop.order.controller;

import com.aniwhere.domain.shop.order.dto.OrderDTO;
import com.aniwhere.domain.shop.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderDTO> getOrdersByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        return orderService.findOrdersByDateRange(startDate, endDate);
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable String orderId) {
        return orderService.findOrderById(orderId);
    }
}