package com.aniwhere.domain.admin.view;

import com.aniwhere.domain.admin.service.AdminService;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.shop.product.domain.Product;
import com.aniwhere.domain.shop.product.dto.ProductDTO;
import com.aniwhere.domain.shop.product.service.ProductService;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminViewController {

    private final AdminService adminService;
    private final HomeService homeService;
    private final ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String userName = homeService.getAuthenticatedUserName();
        int limit = 5;

        List<OrderSucDTO> orders = adminService.selectRecentOrders(limit);
        List<ProductDTO> products = adminService.selectRowQuantityProducts();
        model.addAttribute("name", userName);
        model.addAttribute("orders", orders);
        model.addAttribute("products", products);
        return "admin/admin-dashboard";
    }

    @GetMapping("/products")
    public String products(Model model, @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "9") int size,
                           @RequestParam(defaultValue = "all") String category) {
        String userName = homeService.getAuthenticatedUserName();

        model.addAttribute("name", userName);
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
        return "admin/admin-product";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(defaultValue = "9") int limit, @RequestParam(defaultValue = "0") int offset, Model model) {
        String userName = homeService.getAuthenticatedUserName();
        int totalRoutes = adminService.countOrders();
        int currentPage = offset / limit + 1;
        int totalPages = (int) Math.ceil((double) totalRoutes / limit);
        List<OrderSucDTO> orders = adminService.allOrders(limit, offset);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("limit", limit);
        model.addAttribute("offset", offset);
        model.addAttribute("name", userName);
        model.addAttribute("orders", orders);

        return "admin/admin-orders";
    }

    @GetMapping("/member")
    public String member(Model model, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {

        List<JoinDTO> members = adminService.selectAllShopUsers(limit, offset);
        String userName = homeService.getAuthenticatedUserName();

        int totalRoutes = adminService.shopUserCount();
        int currentPage = offset / limit + 1;
        int totalPages = (int) Math.ceil((double) totalRoutes / limit);

        model.addAttribute("name", userName);
        model.addAttribute("members", members);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("limit", limit);
        model.addAttribute("offset", offset);

        return "admin/admin-member";
    }

    @GetMapping("/admin-list")
    public String adminList(Model model) {
        return "admin/admin-admin-list";
    }


    @GetMapping("/mail")
    public String mail(Model model) {
        String userName = homeService.getAuthenticatedUserName();
        model.addAttribute("name", userName);
        return "admin/admin-mail";
    }

    @GetMapping("/product")
    @ResponseBody
    public Map<String, Object> searchProducts(@RequestParam("keyword") String keyword,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "9") int size) {
        int offset = (page - 1) * size;
        List<Product> products = adminService.searchProducts(keyword, size, offset);
        int totalProducts = adminService.getTotalSearchResults(keyword);
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        response.put("totalProducts", totalProducts);

        return response;
    }

}
