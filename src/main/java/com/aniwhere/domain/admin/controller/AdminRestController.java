package com.aniwhere.domain.admin.controller;

import com.aniwhere.domain.admin.dto.MailDTO;

import com.aniwhere.domain.admin.service.AdminService;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("adminRestController")
@RequestMapping("/admin/dash")
public class AdminRestController {

    private final AdminService adminService;

    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/submit-mail")
    public String submitMail(@RequestBody MailDTO mailDTO) {
        try {
            adminService.saveMail(mailDTO); // 수정: adminService 인스턴스를 사용하여 메서드 호출
            return "success"; // 성공 응답
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // 오류 응답
        }
    }

    @GetMapping("/list/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId) {

        OrderSucDTO orderDTO = adminService.findOrderById(orderId);

        List<OrderDetailDTO> orderDetailDTOs = adminService.findDetailsById(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("order", orderDTO);
        response.put("orderDetails", orderDetailDTOs);

        return ResponseEntity.ok(response);
    }
}


