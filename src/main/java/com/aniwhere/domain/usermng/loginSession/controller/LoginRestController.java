package com.aniwhere.domain.usermng.loginSession.controller;

import com.aniwhere.domain.usermng.loginSession.domain.LoginSession;
import com.aniwhere.domain.usermng.loginSession.service.LoginSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginRestController {

    @Autowired
    private LoginSessionService loginSessionService;

    @GetMapping("/user/{username}")
    public LoginSession getUserByUsername(@PathVariable String username) {
        // 사용자 이름으로 사용자 정보를 가져오는 API 엔드포인트
        return loginSessionService.findByUsername(username);
    }

    @PostMapping("/session")
    public String login(@RequestBody LoginSession loginSession) {
        // 로그인 요청 처리
        boolean isAuthenticated = loginSessionService.authenticate(loginSession.getUsername(), loginSession.getPassword());
        if (isAuthenticated) {
            return "로그인 성공"; // 인증 성공 시 응답
        } else {
            return "로그인 실패"; // 인증 실패 시 응답
        }
    }
}
