package com.aniwhere.domain.admin.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminViewController")
@RequestMapping("/admin")
public class ViewController {

    @GetMapping("/dash")
    public String dashboard() {
        return "animall/shop-admin";
    }

    @GetMapping("/product-list")
    public String productList() {
        return "animall/shop-admin-product-list";
    }

    @GetMapping("/product-reg")
    public String preg() {
        return "animall/shop-admin-product-reg";
    }

    @GetMapping("/product-modi")
    public String modi() {
        return "animall/shop-admin-product-modi";
    }

}
