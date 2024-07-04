package com.aniwhere.domain.admin.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/products")
    public String products() {
        return "admin/AllProduct";
    }

    @GetMapping("/orders")
    public String orders() {
        return "admin/Orders";
    }
}
