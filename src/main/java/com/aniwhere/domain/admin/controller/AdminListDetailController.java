package com.aniwhere.domain.admin.controller;

import com.aniwhere.domain.admin.dto.AdminListDetailDTO;
import com.aniwhere.domain.admin.service.AdminListDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminListDetailController {

    @Autowired
    private AdminListDetailService adminListDetailService;

    @GetMapping("/list")
    public String getAdminList(Model model) {
        List<AdminListDetailDTO> admins = adminListDetailService.getAllAdmins();
        model.addAttribute("admins", admins);
        return "admin-admin-list";
    }

    @GetMapping("/details/{userId}")
    @ResponseBody
    public AdminListDetailDTO getAdminDetails(@PathVariable String userId) {
        return adminListDetailService.getAdminDetails(userId);
    }

    @PostMapping("/update")
    public String updateAdmin(@ModelAttribute AdminListDetailDTO admin) {
        adminListDetailService.updateAdmin(admin);
        return "redirect:/admin/list";
    }
}
