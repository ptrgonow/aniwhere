package com.aniwhere.domain.user.mypage.controller;

import com.aniwhere.domain.user.mypage.domain.UserDetail;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import com.aniwhere.domain.user.mypage.service.MyService;
import jakarta.servlet.http.HttpSession;
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

    private String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principal;
                // OAuth2User로부터 사용자 ID를 추출하는 로직을 여기에 추가
                // 예시: return oauthUser.getAttribute("id");
                return  oauthUser.getAttribute("userId");
            }
        }
        return null;
    }

    @GetMapping("/detail")
    public String my(Model model, HttpSession session) {

        String userId = getAuthenticatedUserId();

        UserDetailDTO userDetail = myService.getUserDetailByUserId(userId);
        System.out.println("왜 안나와" + userDetail.getAddress());

        model.addAttribute("detail", userDetail);

        return "popup/mypage";
    }

}
