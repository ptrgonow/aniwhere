package com.aniwhere.domain.shop.view;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.cart.service.CartService;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.shop.order.service.OrderService;
import com.aniwhere.domain.shop.product.domain.Product;
import com.aniwhere.domain.shop.product.service.ProductService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopViewController {

    private final HomeService homeService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principal;
                return  oauthUser.getAttribute("userId");
            }
        }
        return null;
    }
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
    public String detail(Model model, @RequestParam("id") Integer id)  {

        Product detail = productService.findProductById(id);

        model.addAttribute("details", detail);

        return "animall/shop-product-detail";
    }

    @GetMapping("/cart")
    public String cart(Model model)  {
        String userId = getAuthenticatedUserId();
        List<Cart> cart = cartService.getCartItems(userId);
        model.addAttribute("cart", cart);

        return "animall/shop-cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model){
        String userId = getAuthenticatedUserId();
        UserDetailDTO user = orderMapper.detailByUserId(userId);
        List<Cart> checkedItems = orderService.getCheckedCartItems(userId);
        int totalProductPrice = checkedItems.stream()
                .mapToInt(Cart::getTotalPrice)
                .sum();

        model.addAttribute("orderItems", checkedItems);
        model.addAttribute("totalProductPrice", totalProductPrice);

        model.addAttribute("userinfo", user);
        return "animall/shop-checkout";
    }

    @GetMapping("/summary")
    public String summary(){
        return "animall/shop-summary";
    }

}


