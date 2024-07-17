package com.aniwhere.domain.admin.controller;

import com.aniwhere.domain.admin.dto.AdminListDetailDTO;
import com.aniwhere.domain.admin.service.AdminListDetailService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminListDetailController")
@RequestMapping("/admin/regi")
public class AdminListDetailController {

    private final AdminListDetailService adminListDetailService;
    private final HomeService homeService;

    @Autowired
    public AdminListDetailController(AdminListDetailService adminListDetailService, HomeService homeService) {
        this.adminListDetailService = adminListDetailService;
        this.homeService = homeService;
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<AdminListDetailDTO> getAdminById(@PathVariable String userId) {
        AdminListDetailDTO admin = adminListDetailService.getAdminById(userId);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdminListDetailDTO>> getAllAdmins() {
        List<AdminListDetailDTO> admins = adminListDetailService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @PutMapping("/details/update")
    public ResponseEntity<String> updateAdmin(@RequestBody AdminListDetailDTO admin) {
        boolean updated = adminListDetailService.updateAdmin(admin);
        if (updated) {
            return ResponseEntity.ok("Admin updated successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to update admin");
        }
    }
}
