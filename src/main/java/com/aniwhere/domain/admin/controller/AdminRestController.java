package com.aniwhere.domain.admin.controller;

import com.aniwhere.domain.admin.dto.MailDTO;

import com.aniwhere.domain.admin.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminRestController")
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
}


