package com.aniwhere.domain.shop.view;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.cart.service.CartService;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.shop.order.service.OrderService;
import com.aniwhere.domain.shop.product.domain.Product;
import com.aniwhere.domain.shop.product.service.ProductService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String shopMain(Model model, @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "9") int size,
                           @RequestParam(defaultValue = "all") String category)  {

        String userName = homeService.getAuthenticatedUserName();
        if (userName == null) {
            return "redirect:/login";
        }

        int totalProducts;
        List<Product> products = switch (category) {
            case "dog" -> {
                totalProducts = productService.getTotalDogProducts();
                yield productService.findDog(size, (page - 1) * size);
            }
            case "cat" -> {
                totalProducts = productService.getTotalCatProducts();
                yield productService.findCat(size, (page - 1) * size);
            }
            case "other" -> {
                totalProducts = productService.getTotalOtherProducts();
                yield productService.findOthers(size, (page - 1) * size);
            }
            default -> {
                totalProducts = productService.getTotal();
                yield productService.findAllProductsWithLimit(size, (page - 1) * size);
            }
        };

        int totalPages = (int) Math.ceil((double) totalProducts / size);

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("name", userName);
        model.addAttribute("category", category);

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

            List<OrderSucDTO> orderList = orderService.getOrderItemsById(userId, orderId);

            model.addAttribute("name", userName);
            model.addAttribute("userinfo", user);
            model.addAttribute("orders", orderList);

            return "animall/shop-checkout";
        } catch (Exception e) {
            return "error/404-error";
        }
    }

    @GetMapping("/success")
    public String success(){
        String userId = homeService.getAuthenticatedUserId();
        orderService.deleteFromCart(userId);

        return "animall/shop-checkout-success";
    }

    @GetMapping("/product")
    @ResponseBody
    public Map<String, Object> searchProducts(@RequestParam("keyword") String keyword,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "9") int size) {
        int offset = (page - 1) * size;
        List<Product> products = productService.searchProducts(keyword, size, offset);
        int totalProducts = productService.getTotalSearchResults(keyword);
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalProducts", totalProducts);

        return response;
    }

    @GetMapping("/search")
    public String search(Model model){
        String userId = homeService.getAuthenticatedUserId();
        String userName = homeService.getAuthenticatedUserName();

        model.addAttribute("name", userName);
        model.addAttribute("userId", userId);
        return "animall/shop-order-search";
    }

}


