package com.aniwhere.domain.main;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    // 메인 페이지 이동
    // (로그인 정보를 가져와서 모델에 담아서 index.html 로 이동)
    // (OAuth2User : OAuth2 로그인 정보를 담는 인터페이스)
    // (UserDetails : 사용자의 정보를 담는 인터페이스)
    // (SecurityContextHolder : 현재 사용자의 인증 정보를 가져오는 클래스)
    // (instanceof : 객체가 특정 클래스/인터페이스의 인스턴스인지 확인하는 연산자 / 형변환 필요 없음 / Java 16 부터 사용 가능)
    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof OAuth2User oauthUser) {
                model.addAttribute("name", oauthUser.getAttribute("name"));
                model.addAttribute("email", oauthUser.getAttribute("email"));
            } else if (principal instanceof UserDetails formUser) {
                model.addAttribute("name", formUser.getUsername());
            }
        }
        return "index"; // index.html 템플릿으로 이동
    }

    // TODO : 진석이한테 위치 변경하라고 할 것 (둘 다 board 로 이동)
    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
