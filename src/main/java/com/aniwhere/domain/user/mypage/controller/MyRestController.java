package com.aniwhere.domain.user.mypage.controller;

import com.aniwhere.domain.user.mypage.dto.PwdCheckDTO;
import com.aniwhere.domain.user.mypage.dto.UpdateDetailDTO;
import com.aniwhere.domain.user.mypage.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mypage")
@AllArgsConstructor
public class MyRestController {

    private final MyService myService;

    @GetMapping("/authenticatedUserId")
    public String getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauthUser = (OAuth2User) principal;
                return oauthUser.getAttribute("userId");
            }
        }
        return null;
    }

    @PostMapping("/pwdcheck")
    public ResponseEntity<String> checkPassword(@RequestBody PwdCheckDTO pwdCheckDTO) {
        boolean isPasswordMatch = myService.checkPassword(pwdCheckDTO.getUserId(), pwdCheckDTO.getUserPwd());

        if (isPasswordMatch) {
            return ResponseEntity.ok("비밀번호가 일치합니다");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateDetailDTO updateDetail) {
        try {
            boolean isUpdated = myService.updateUser(updateDetail);
            if (isUpdated) {
                return ResponseEntity.ok("정보 수정이 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("정보 수정에 실패했습니다.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
