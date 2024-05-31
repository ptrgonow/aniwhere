package com.aniwhere.domain.shop.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller("shopViewController")
@RequestMapping("/shop")
public class ViewController {


    @GetMapping("/main")
    public String shopMain()  {
        return "animall/shop-main";
    }

    @GetMapping("/cart")
    public String cart()  {
        return "animall/shop-cart";
    }

    @GetMapping("/detail")
    public String detail()  {
        return "animall/shop-product-detail";
    }

    @GetMapping("/purchase")
    public String purchase()  {
        return "animall/shop-purchase";
    }

}
