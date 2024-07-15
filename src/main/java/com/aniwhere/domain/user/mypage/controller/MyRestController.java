package com.aniwhere.domain.user.mypage.controller;

import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.mypage.dto.PwdCheckDTO;
import com.aniwhere.domain.user.mypage.dto.UpdateDetailDTO;
import com.aniwhere.domain.user.mypage.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/mypage")
@AllArgsConstructor
public class MyRestController {

    private final MyService myService;
    private final HomeService homeService;

    @PostMapping("/pwdcheck")
    public ResponseEntity<String> checkPassword(@RequestBody PwdCheckDTO pwdCheckDTO) {
        boolean isPasswordMatch = myService.checkPassword(pwdCheckDTO.getUserId(), pwdCheckDTO.getUserPwd());

        if (isPasswordMatch) {
            return ResponseEntity.ok("비밀번호가 일치합니다");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
        }
    }

    @GetMapping("/checkdupl")
    public ResponseEntity<Map<String, Boolean>> checkDuplicates(@RequestParam String userName, @RequestParam String phone) {
        String userId = homeService.getAuthenticatedUserId();
        Map<String, Boolean> response = new HashMap<>();
        response.put("nameExists", myService.existsByName(userName, userId));
        response.put("phoneExists", myService.existsByPhone(phone, userId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateDetailDTO updateDetail) {
        try {
            String userId = homeService.getAuthenticatedUserId();
            String userPhone = myService.getUserPhone(userId);
            String userName = homeService.getAuthenticatedUserName();

            boolean isNameExists = myService.existsByName(updateDetail.getUserName(), userId);
            boolean isPhoneExists = myService.existsByPhone(updateDetail.getPhone(), userId);

            if (isNameExists && !updateDetail.getUserName().equals(userName)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 이름입니다. 다시 입력하세요");
            } else if (isPhoneExists && !updateDetail.getPhone().equals(userPhone)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용중인 휴대폰입니다. 다시 입력하세요");
            }

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

    @DeleteMapping("/userdel/{userId}")
    public ResponseEntity<Void> userDelete(@PathVariable("userId") String userId){
        myService.deleteUser(userId);
        return ResponseEntity.ok().build();
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
