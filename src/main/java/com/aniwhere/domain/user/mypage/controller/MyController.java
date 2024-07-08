package com.aniwhere.domain.user.mypage.controller;

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


@Controller
@RequestMapping("/mypage")
@AllArgsConstructor
public class MyController {

    private final MyService myService;

    // 스프링 프레임워크로 해당 유저ID 가져오는 로직
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

    @GetMapping("/detail")
    public String my(Model model) {

        String userId = getAuthenticatedUserId();

        UserDetail userDetail = myService.getUserDetailByUserId(userId);

        model.addAttribute("detail", userDetail);

        return "popup/mypage";
    }

}
