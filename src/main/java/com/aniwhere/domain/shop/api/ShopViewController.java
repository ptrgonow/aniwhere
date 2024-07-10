package com.aniwhere.domain.shop.api;

import com.aniwhere.domain.shop.product.domain.Product;
import com.aniwhere.domain.shop.product.service.ProductService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopViewController {

    private final HomeService homeService;
    private final ProductService productService;

    @GetMapping("/main")
    public String shopMain(Model model,@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "9") int size)  {


        String userName = homeService.getAuthenticatedUserName();
        if (userName == null) {
            return "redirect:/login";
        }

        int totalProducts = productService.getTotal();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        int offset = (page - 1) * size;

        List<Product> products = productService.findAllProductsWithLimit(size, offset);
        List<Product> dogs = productService.findDog(size, offset);
        List<Product> cats = productService.findCat(size, offset);
        List<Product> others = productService.findOthers(size, offset);

        model.addAttribute("products", products);
        model.addAttribute("dog", dogs);
        model.addAttribute("cat", cats);
        model.addAttribute("other", others);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "animall/shop-main";
    }

    @GetMapping("/detail")
    public String detail(Model model)  {
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "animall/shop-product-detail";
    }

    @GetMapping("/cart")
    public String cart(Model model)  {
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "animall/shop-cart";
    }

    @GetMapping("/review")
    public String review(Model model){
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "animall/shop-review";
    }

    @GetMapping("/review-single")
    public String reviewSingle(Model model){
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "animall/shop-review-single";
    }

    @GetMapping("/checkout")
    public String checkout(Model model){
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "animall/shop-checkout";
    }

    @GetMapping("/order-search")
    public String orderSearch(Model model){
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "animall/shop-order-search";
        }

    @GetMapping("/order-summary")
        public String orderSummary(Model model){
        model.addAttribute("name", homeService.getAuthenticatedUserName());
        model.addAttribute("userId", homeService.getAuthenticatedUserId());
        return "animall/shop-order-summary";
        }
    }

