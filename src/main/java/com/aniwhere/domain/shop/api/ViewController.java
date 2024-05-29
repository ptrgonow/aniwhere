package com.aniwhere.domain.shop.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("shopViewController")
@RequestMapping("/shop")
public class ViewController {

    @GetMapping("/cart")
    public String cart()  {return "animall/shop-cart";
    }
}
