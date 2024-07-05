package com.aniwhere.domain.user.join.controller;

import com.aniwhere.domain.user.join.domain.Join;
import com.aniwhere.domain.user.join.dto.JoinResponseDTO;
import com.aniwhere.domain.user.join.service.JoinService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class JoinController {

    private final JoinService joinService;


    @PostMapping("/joinProc")
    public String joinProc(@RequestBody Join join) {
        boolean success = joinService.joinProcess(join);
        return success ? "success" : "fail";
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkDuplicate( @RequestParam("type") String type, @RequestParam("value") String value ) {
        boolean isDuplicate = joinService.isDuplicate(
                type.equals("userId") ? value : null,
                type.equals("email") ? value : null,
                type.equals("phone") ? value : null
        );
        return ResponseEntity.ok(isDuplicate);
    }

}
