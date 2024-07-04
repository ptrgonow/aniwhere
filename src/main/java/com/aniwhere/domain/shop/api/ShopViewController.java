package com.aniwhere.domain.shop.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopViewController {

    @GetMapping("/main")
    public String shopMain()  {
        return "animall/shop-main";
    }

    @GetMapping("/detail")
    public String detail()  {
        return "animall/shop-product-detail";
    }

}
