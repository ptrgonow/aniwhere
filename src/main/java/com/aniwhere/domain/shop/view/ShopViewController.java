package com.aniwhere.domain.shop.view;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.cart.service.CartService;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.shop.order.dto.OrderDTO;
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
        model.addAttribute("name", userName);

        return "animall/shop-main";
    }

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam("id") Integer id)  {
        String userName = homeService.getAuthenticatedUserName();
        String userId = homeService.getAuthenticatedUserId();
        Product detail = productService.findProductById(id);
        model.addAttribute("details", detail);
        model.addAttribute("name", userName);
        model.addAttribute("userId", userId);

        return "animall/shop-product-detail";
    }

    @GetMapping("/cart")
    public String cart(Model model)  {
        String userId = homeService.getAuthenticatedUserId();
        String userName = homeService.getAuthenticatedUserName();
        List<Cart> cart = cartService.getCartItems(userId);
        model.addAttribute("cart", cart);
        model.addAttribute("name", userName);

        return "animall/shop-cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, @RequestParam("orderId") String orderId){
        try {
            String userId = homeService.getAuthenticatedUserId();
            String userName = homeService.getAuthenticatedUserName();
            UserDetailDTO user = orderMapper.detailByUserId(userId);

            List<OrderDTO> orderList = orderService.getOrderItemsById(userId, orderId);

            model.addAttribute("name", userName);
            model.addAttribute("userinfo", user);
            model.addAttribute("orders", orderList);

            return "animall/shop-checkout";
        } catch (Exception e) {
            return "error/404-error";
        }
    }
    @GetMapping("/success")
    public String success(@RequestParam("userId") String userId){

        orderService.deleteFromCart(userId);

        return "animall/shop-order-search";
    }

    @GetMapping("/summary")
    public String summary(){
        return "animall/shop-order-summary";
    }
    @GetMapping("/search")
    public String search(){
        return "animall/shop-order-search";
    }

}


