package com.aniwhere.domain.admin.controller;

import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.admin.service.AdminService;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.user.join.service.MailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

@RestController("adminRestController")
@RequestMapping("/admin/dash")
public class AdminRestController {

    // private final AdminService adminService; - AdminService 클래스의 인스턴스를 주입받아 필드에 저장합니다.
    // final로 선언되어 있어 변경할 수 없습니다.
    //public AdminRestController(AdminService adminService) - 생성자를 통해 AdminService 인스턴스를 주입받습니다.
    // 생성자 주입 방식은 Spring에서 의존성 주입을 할 때 일반적으로 사용하는 방식입니다.
    private final AdminService adminService;
    private MailService mailService;

    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/submit-mail")
    public Map<String, String> submitMail(@RequestBody MailDTO mailDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            adminService.saveMailAndSendToAllUsers(mailDTO);
            response.put("status", "success");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @GetMapping("/member/empty")
    public ResponseEntity<Map<String, Object>> emptyMember(@RequestParam String type,
                                                           @RequestParam(defaultValue = "10") int limit,
                                                           @RequestParam(defaultValue = "0") int offset) {
        List<JoinDTO> members;
        int totalMember;

        switch (type) {
            case "all":
            default:
                members = adminService.allMembers(limit, offset);
                totalMember = adminService.memberCount();
                break;
            case "address":
                members = adminService.emptyAdressUsers(limit, offset);
                totalMember = adminService.countAddUsers();
                break;
            case "phone":
                members = adminService.emptyPhoneUsers(limit, offset);
                totalMember = adminService.countPhoneUsers();
                break;
        }

        int currentPage = offset / limit + 1;
        int totalPages = (int) Math.ceil((double) totalMember / limit);

        Map<String, Object> response = new HashMap<>();

        response.put("members", members);
        response.put("currentPage", currentPage);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/search")
    public ResponseEntity<Map<String, Object>> searchUser(@RequestParam(required = false) String userId,
                                                          @RequestParam(defaultValue = "10") int limit,
                                                          @RequestParam(defaultValue = "0") int offset) {
        Map<String, Object> response = adminService.searchUser(userId, limit, offset);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/member/mailsend")
    public String sendMail(@RequestParam List<String> to, @RequestParam String subject, @RequestParam String body) {
        try {
            mailService.sendBulkHtmlMessage(to, subject, body);
            return "이메일 전송 성공!";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "이메일 전송 실패";
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

    @PostMapping("/status/{orderId}")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody Map<String, String> requestBody) {

        String newStatus = requestBody.get("newStatus");
            adminService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok(Map.of("success", true, "message", "주문 상태 업데이트 성공"));
    }
}
