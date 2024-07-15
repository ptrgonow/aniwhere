package com.aniwhere.domain.user.join.controller;

import com.aniwhere.domain.user.join.domain.Join;
import com.aniwhere.domain.user.join.service.JoinService;

import com.aniwhere.domain.user.join.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private final MailService mailService;

    @PostMapping("/joinProc")
    public ResponseEntity<String> joinProc(@RequestBody Join join) {
        System.out.println("Received join request: " + join); // 로그 추가
        boolean success = joinService.joinProcess(join);
        System.out.println("Join process result: " + success); // 로그 추가
        return success ? ResponseEntity.ok("success") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkExists(@RequestParam String type, @RequestParam String value) {
        boolean exists = switch (type) {
            case "userId" -> joinService.isUserIdExists(value);
            case "email" -> joinService.isEmailExists(value);
            case "phone" -> joinService.isPhoneExists(value);
            case "userName" -> joinService.isUserNameExists(value);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/sendemail")
    public String sendAuthCode(@RequestParam String email) {
        try {
            int num = mailService.sendEmail(email);
            return "인증코드 발급 : " + num;
        } catch (Exception e) {
            e.printStackTrace();
            return "이메일 전송 실패: " + e.getMessage();
        }
    }

}
