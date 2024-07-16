package com.aniwhere.domain.admin.view;

import com.aniwhere.domain.admin.service.AdminService;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String orders(@RequestParam(defaultValue = "9") int limit, @RequestParam(defaultValue = "0") int offset, Model model) {
        String userName = homeService.getAuthenticatedUserName();
        int totalRoutes = adminService.countOrders();
        int currentPage = offset / limit + 1;
        int totalPages = (int) Math.ceil((double) totalRoutes / limit);
        List<OrderSucDTO> orders = adminService.allOrders(limit, offset);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("limit", limit);
        model.addAttribute("offset", offset);
        model.addAttribute("name", userName);
        model.addAttribute("orders", orders);

        return "admin/admin-orders";
    }

    @GetMapping("/member")
    public String member(Model model, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {

        List<JoinDTO> members = adminService.allMembers(limit, offset);
        String userName = homeService.getAuthenticatedUserName();

        int totalRoutes = adminService.memberCount();
        int currentPage = offset / limit + 1;
        int totalPages = (int) Math.ceil((double) totalRoutes / limit);

        model.addAttribute("name", userName);
        model.addAttribute("members", members);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("limit", limit);
        model.addAttribute("offset", offset);

        return "admin/admin-member";
    }

    @GetMapping("/mail")
    public String mail(Model model) {
        String userName = homeService.getAuthenticatedUserName();
        model.addAttribute("name", userName);
        return "admin/admin-mail";
    }
}
