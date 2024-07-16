package com.aniwhere.domain.admin.controller;

import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.admin.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("adminRestController")
public class AdminRestController {

    // private final AdminService adminService; - AdminService 클래스의 인스턴스를 주입받아 필드에 저장합니다.
    // final로 선언되어 있어 변경할 수 없습니다.
    //public AdminRestController(AdminService adminService) - 생성자를 통해 AdminService 인스턴스를 주입받습니다.
    // 생성자 주입 방식은 Spring에서 의존성 주입을 할 때 일반적으로 사용하는 방식입니다.
    private final AdminService adminService;

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
}
