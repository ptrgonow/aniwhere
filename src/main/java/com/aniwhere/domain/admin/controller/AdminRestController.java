package com.aniwhere.domain.admin.controller;

import com.aniwhere.domain.admin.dto.MailDTO;

import com.aniwhere.domain.admin.service.AdminService;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/member-empty")
    public ResponseEntity<Map<String, Object>> emptyMember(@RequestParam String type,
                                           @RequestParam(defaultValue = "10") int limit,
                                           @RequestParam(defaultValue = "0") int offset){
        List<JoinDTO> members;
        int totalMember;

        switch (type){
            case "all" :
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
}


