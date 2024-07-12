package com.aniwhere.domain.admin.view;

import com.aniwhere.domain.admin.service.AdminService;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final AdminService adminService;

    public AdminViewController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/admin-dashboard";
    }

    @GetMapping("/products")
    public String products() {
        return "admin/admin-product";
    }

    @GetMapping("/orders")
    public String orders() {
        return "admin/admin-orders";
    }

    @GetMapping("/member")
    public String member(Model model) {

        List<JoinDTO> members = adminService.allMembers();
        model.addAttribute("members", members);

        return "admin/admin-member";
    }
}
