package com.aniwhere.domain.shop.review.controller;

import com.aniwhere.domain.shop.review.dto.ReviewDTO;
import com.aniwhere.domain.shop.review.service.ReviewService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 <p>
 `@RestController` 어노테이션 :
<br>
 스프링 4.0부터 추가된 어노테이션으로, 해당 컨트롤러 클래스에 `@Controller`와 `@ResponseBody` 어노테이션을 합친 것이다.
 즉, 해당 컨트롤러 클래스의 모든 메서드에 `@ResponseBody` 어노테이션이 적용된 것과 같다.
 </p>
 <br>
 <p>
 `@RequestMapping` 어노테이션 :
<br>
 요청 URL과 컨트롤러 클래스 또는 메서드를 매핑하는 어노테이션.
 클래스에 적용하면 해당 클래스의 모든 메서드에 적용된다.
 </p>
 <br>
 <p>
 `@AllArgsConstructor` 어노테이션 :
 <br>
    해당 클래스의 모든 필드를 파라미터로 받는 생성자를 생성해준다.
 </p>
 */

@RestController
@RequestMapping("/review")
@AllArgsConstructor
public class ReviewController {

    // ReviewService 객체 주입 (spring container에 의해 자동으로 주입 : IoC, DI)
    private final ReviewService reviewService;

    /**
     <p>
     `@GetMapping` 어노테이션 :
        <br>
        HTTP GET 요청을 처리하는 핸들러 메서드임을 나타내는 어노테이션.<br>
        `@RequestMapping(method = RequestMethod.GET)`과 동일한 기능을 한다.
        <br>
        `@GetMapping("/list")` : "/review/list" URL로 GET 메서드가 처리한다.
     </p>
        <br>
        <p>
     `PostMapping` 어노테이션 :
        <br>
        HTTP POST 요청을 처리하는 핸들러 메서드임을 나타내는 어노테이션.<br>
        `@RequestMapping(method = RequestMethod.POST)`과 동일한 기능을 한다.
        <br>
        `@PostMapping("/insert")` : "/review/insert" URL로 POST 메서드가 처리한다.
        </p>
     */
    @GetMapping("/list/{productId}")
    public List<ReviewDTO> getAllReview(@PathVariable int productId) {
        return reviewService.getAllReview(productId);
    }

    @GetMapping("/{reviewId}")
    public ReviewDTO getReview(@PathVariable int reviewId) {
        return reviewService.getReview(reviewId);
    }

    @PostMapping("/insert")
    public void insertReview(@RequestBody ReviewDTO reviewDTO) {
        reviewService.insertReview(reviewDTO);
    }

    @PutMapping("/update")
    public void updateReview(@RequestBody ReviewDTO reviewDTO) {
        reviewService.updateReview(reviewDTO);
    }

    @DeleteMapping("/delete/{reviewId}")
    public void deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @PostMapping("/like/{reviewId}")
    public void increaseLikeCount(@PathVariable int reviewId) {
        reviewService.increaseLikeCount(reviewId);
    }

}
