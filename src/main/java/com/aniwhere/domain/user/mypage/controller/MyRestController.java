package com.aniwhere.domain.user.mypage.controller;

import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.user.mypage.dto.PwdCheckDTO;
import com.aniwhere.domain.user.mypage.dto.UpdateDetailDTO;
import com.aniwhere.domain.user.mypage.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mypage")
@AllArgsConstructor
public class MyRestController {

    private final MyService myService;

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

    // JSON 데이터를 반환하는 엔드포인트
    @GetMapping("/one/routedetail/{id}")
    public ResponseEntity<RouteDTO> markerDetail(@PathVariable("id") Long id) {
        RouteDTO routeDetail = myService.getRouteDetailById(id);

        return ResponseEntity.ok(routeDetail);
    }

    @PostMapping("/updateroute/{id}")
    public ResponseEntity<Void> updateRoute(@PathVariable("id") Long id, @RequestBody RouteDTO routeDTO) {
        try {
            routeDTO.setId(id);
            myService.updateRoute(routeDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 예외 발생 시 System.out.println으로 출력
            System.out.println("Error updating route in controller: " + e.getMessage());
            e.printStackTrace(); // 스택 트레이스 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delroute/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable("id") long id) {
        try {
            myService.deleteRoute(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Error deleting route in controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
