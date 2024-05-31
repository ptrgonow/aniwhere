package com.aniwhere.domain.shop.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("paymentController")
@RequestMapping("/pay")
public class PaymentRestController {

    @GetMapping("/view")
    public String view() {
        return "animall/shop-purchase";
    }

}
