package com.aniwhere.domain.admin.view;

import com.aniwhere.domain.admin.service.AdminService;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminViewController {

    private final AdminService adminService;
    private final HomeService homeService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String userName = homeService.getAuthenticatedUserName();
        model.addAttribute("name", userName);
        return "admin/admin-dashboard";
    }

    @GetMapping("/products")
    public String products(Model model) {
        String userName = homeService.getAuthenticatedUserName();
        model.addAttribute("name", userName);
        return "admin/admin-product";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        String userName = homeService.getAuthenticatedUserName();
        model.addAttribute("name", userName);
        return "admin/admin-orders";
    }

    @GetMapping("/member")
    public String member(Model model) {

        List<JoinDTO> members = adminService.allMembers();
        String userName = homeService.getAuthenticatedUserName();

        model.addAttribute("name", userName);
        model.addAttribute("members", members);

        return "admin/admin-member";
    }

    @GetMapping("/mail")
    public String mail(Model model) {
        String userName = homeService.getAuthenticatedUserName();
        model.addAttribute("name", userName);
        return "admin/admin-mail";
    }

}
