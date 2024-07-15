package com.aniwhere.domain.user.mypage.controller;

import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.route.service.RouteService;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.mypage.domain.UserDetail;
import com.aniwhere.domain.user.mypage.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/mypage")
@AllArgsConstructor
public class MyController {

    private final MyService myService;
    private final HomeService homeService;

    @GetMapping("/detail")
    public String my(Model model) {

        String userId = homeService.getAuthenticatedUserId();
        UserDetail userDetail = myService.getUserDetailByUserId(userId);
        boolean isSocial = myService.isSocialUser(userId);

        model.addAttribute("detail", userDetail);
        model.addAttribute("name", userDetail.getUserName());
        model.addAttribute("userId", userId);
        model.addAttribute("isSocial", isSocial);

        return "/mypage/modify";

    }

    @GetMapping("/routedetail/{id}")
    public String routeDetail(@PathVariable("id") Long id, Model model) {

        RouteDTO routeDetail = myService.getRouteDetailById(id);
        String userName = homeService.getAuthenticatedUserName();
        String userId = homeService.getAuthenticatedUserId();

        model.addAttribute("routeDetail", routeDetail);
        model.addAttribute("name", userName);
        model.addAttribute("id", userId);

        return "/mypage/route-detail";
    }

    @GetMapping("/routelist")
    public String routeList(@RequestParam(defaultValue = "3") int limit, @RequestParam(defaultValue = "0") int offset, Model model) {

        String userId = homeService.getAuthenticatedUserId();
        String userName = homeService.getAuthenticatedUserName();
        List<RouteDTO> userRouteList = myService.selectRouteByUserId(userId, limit, offset);

        int totalRoutes = myService.countUserRoutes(userId);
        int currentPage = offset / limit + 1;
        int totalPages = (int) Math.ceil((double) totalRoutes / limit);

        model.addAttribute("userRoutes", userRouteList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("limit", limit);
        model.addAttribute("offset", offset);

        model.addAttribute("userRouteList", userRouteList);
        model.addAttribute("name", userName);
        model.addAttribute("id", userId);

        return "/mypage/route-list";
    }



}
