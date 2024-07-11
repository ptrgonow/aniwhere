package com.aniwhere.domain.user.mypage.controller;

import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.route.service.RouteService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.mypage.domain.UserDetail;
import com.aniwhere.domain.user.mypage.service.MyService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/mypage")
@AllArgsConstructor
public class MyController {

    private final MyService myService;
    private final HomeService homeService;
    private final RouteService routeService;


    @GetMapping("/detail")
    public String my(Model model) {

        String userId = homeService.getAuthenticatedUserId();
        UserDetail userDetail = myService.getUserDetailByUserId(userId);
        model.addAttribute("detail", userDetail);

        return "/mypage/modify";
    }

    @GetMapping("/routedetail")
    public String routeDetail(@RequestParam("id") String id, Model model) {

        return "/mypage/route-detail";
    }

    @GetMapping("/routelist")
    public String routeList(@RequestParam(defaultValue = "3") int limit, @RequestParam(defaultValue = "0") int offset, Model model) {

        String authenticatedUserId = homeService.getAuthenticatedUserId();
        List<RouteDTO> userRouteList = myService.selectRouteByUserId(authenticatedUserId, limit, offset);

        int totalRoutes = myService.countUserRoutes(authenticatedUserId);
        int currentPage = offset / limit + 1;
        int totalPages = (int) Math.ceil((double) totalRoutes / limit);

        // 디버깅 로그 추가
        System.out.println("Total Routes: " + totalRoutes);
        System.out.println("Current Page: " + currentPage);
        System.out.println("Total Pages: " + totalPages);

        model.addAttribute("userRoutes", userRouteList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("limit", limit);
        model.addAttribute("offset", offset);

        model.addAttribute("userRouteList", userRouteList);
        model.addAttribute("name", homeService.getAuthenticatedUserName());

        return "/mypage/route-list";
    }



}
