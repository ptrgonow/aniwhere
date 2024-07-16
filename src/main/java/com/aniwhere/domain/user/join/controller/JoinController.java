package com.aniwhere.domain.user.join.controller;

import com.aniwhere.domain.user.join.domain.Join;
import com.aniwhere.domain.user.join.dto.MailCodeCheckDTO;
import com.aniwhere.domain.user.join.service.JoinService;

import com.aniwhere.domain.user.join.service.MailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private final MailService mailService;
    private final Map<String, String> authCodeStorage = new ConcurrentHashMap<>();

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
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String authCode = mailService.sendEmail(email);
        return ResponseEntity.ok(authCode);
    }

    // 인증코드 확인
    @PostMapping("/verifycode")
    public ResponseEntity<Boolean> confirmEmail(@RequestBody MailCodeCheckDTO mailCodeCheckDTO) {
        boolean result = mailService.confirmEmail(mailCodeCheckDTO);
        return ResponseEntity.ok(result);
    }

}
