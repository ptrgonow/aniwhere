package com.aniwhere.domain.admin.controller;

import com.aniwhere.domain.admin.dto.AdminListDetailDTO;
import com.aniwhere.domain.admin.service.AdminListDetailService;
import com.aniwhere.domain.user.loginSession.dto.LoginDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController("adminListDetailController")
@RequestMapping("/admin/regi")
public class AdminListDetailController {

    private final AdminListDetailService adminListDetailService;
    private final HomeService homeService;
    private static final Logger logger = LoggerFactory.getLogger(AdminListDetailController.class);

    @Autowired
    public AdminListDetailController(AdminListDetailService adminListDetailService, HomeService homeService) {
        this.adminListDetailService = adminListDetailService;
        this.homeService = homeService;
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<AdminListDetailDTO> getAdminById(@PathVariable String userId) {
        logger.debug("Received request to fetch admin details for userId: {}", userId);
        AdminListDetailDTO admin = adminListDetailService.getAdminById(userId);
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllAdmins(
            @RequestParam(defaultValue = "3") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "ROLE_ADMIN") String role) {
        logger.debug("Received request to fetch all admins with limit {} and offset {} for role {}", limit, offset, role);
        Map<String, Object> response = adminListDetailService.getAllAdmins(limit, offset, role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchAdmin(
            @RequestParam String query,
            @RequestParam(defaultValue = "3") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "ROLE_ADMIN") String role) {
        logger.debug("Received request to search admin by query: {} with limit {} and offset {}", query, limit, offset);
        Map<String, Object> response = adminListDetailService.searchAdmin(query, limit, offset, role);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/details/update")
    public ResponseEntity<String> updateAdmin(@RequestBody AdminListDetailDTO admin) {
        logger.debug("Received request to update admin: {}", admin);
        boolean updated = adminListDetailService.updateAdmin(admin);
        if (updated) {
            return ResponseEntity.ok("Admin updated successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to update admin");
        }
    }

    @GetMapping("/userinfo")
    public ResponseEntity<LoginDTO> getAuthenticatedUserInfo() {
        logger.debug("Received request to fetch authenticated user info");
        String userName = homeService.getAuthenticatedUserName();
        String userId = homeService.getAuthenticatedUserId();

        LoginDTO userInfo = new LoginDTO();
        userInfo.setUserName(userName);
        userInfo.setUserId(userId);

        logger.debug("Authenticated user info: {}", userInfo);

        return ResponseEntity.ok(userInfo);
    }
}

