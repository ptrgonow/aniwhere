package com.aniwhere.domain.shop.api;

import com.aniwhere.domain.user.loginSession.service.HomeService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopViewController {

    private final HomeService homeService;

    @GetMapping("/main")
    public String shopMain(Model model)  {

        String userName = homeService.getAuthenticatedUserName();
        if (userName == null) {
            return "redirect:/login";
        }

        model.addAttribute("name", userName);
        return "animall/shop-main";
    }

    @GetMapping("/detail")
    public String detail()  {
        return "animall/shop-product-detail";
    }

    @GetMapping("/cart")
    public String cart()  {
        return "animall/shop-cart";
    }

    @GetMapping("/review")
    public String review(){
        return "animall/shop-review";
    }

    @GetMapping("/review-single")
    public String reviewSingle(){
        return "animall/shop-review-single";
    }

    @GetMapping("/check-out")
    public String checkOut(){
        return "animall/shop-check-out";
    }


}
