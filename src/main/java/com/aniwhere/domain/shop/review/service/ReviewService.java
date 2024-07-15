package com.aniwhere.domain.shop.review.service;

import com.aniwhere.domain.shop.mapper.ReviewMapper;
import com.aniwhere.domain.shop.review.domain.Review;
import com.aniwhere.domain.shop.review.dto.ReviewDTO;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 <p>
 `@Service` 어노테이션 :
 <br>
 해당 클래스가 비즈니스 로직을 담당하는 서비스 클래스임을 나타내는 어노테이션.
 </p>
 <br>
 <p>
 `@AllArgsConstructor` 어노테이션 :
 <br>
 해당 클래스의 모든 필드를 파라미터로 받는 생성자를 생성해준다.
 </p>
 <br>
 <p>
 `@Transactional` 어노테이션 :
 <br>
 해당 클래스의 모든 메서드에 트랜잭션을 적용한다.<br>
 현재 클래스가 단일 데이터베이스 작업을 수행하므로 `@Transactional` 어노테이션을 적용하지 않아도 된다.<br>
 허나 추후 여러 데이터베이스 작업을 수행할 때는 메서드에 `@Transactional` 어노테이션을 적용해야 한다.
 </p>
 */

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    /**
     <p>
     Stream API :
     <br><br>
     자바 8부터 추가된 API로, 컬렉션, 배열 등의 데이터를 함수형 프로그래밍 방식으로 처리할 수 있다.
     Stream API는 데이터를 추상화하여 다루기 때문에 데이터 소스에 상관없이 동일한 방식으로 데이터를 처리할 수 있다.
     Stream API는 중간 연산과 최종 연산으로 구분되며, 중간 연산은 다른 스트림을 반환하고 최종 연산은 다른 타입의 결과를 반환한다.
     <br><br>
     1. Stream 생성
     <br>
     스트림은 데이터 소스(컬렉션, 배열 등)에서 생성
     <br><br>
     2. 중간 연산
     <br>
     스트림을 반환하며, 중간 연산은 여러 개를 연결할 수 있다.<br>
     (ex. filter, map, sorted, distinct, limit, skip, flatMap 등)<br>
     `map` : 스트림의 각 요소에 함수를 적용하여 새로운 스트림을 반환<br>
     `filter` : 주어진 조건에 맞는 요소만으로 이루어진 새로운 스트림을 반환<br>
     `sorted` : 스트림의 요소를 정렬하여 새로운 스트림을 반환<br>
     `distinct` : 스트림의 요소 중 중복된 요소를 제거한 새로운 스트림을 반환<br>
     `limit` : 스트림의 요소 중 일부를 제한한 새로운 스트림을 반환<br>
     `skip` : 스트림의 요소 중 일부를 건너뛴 새로운 스트림을 반환<br>
     `flatMap` : 스트림의 각 요소에 대해 함수를 적용하여 생성된 스트림을 하나로 병합하여 반환<br>
     <br><br>
     3. 최종 연산
     <br>
     스트림을 반환하지 않으며, 스트림을 닫는 연산이다.<br>
     (ex. forEach, collect, reduce, count, anyMatch, allMatch, noneMatch, findFirst, findAny 등)<br>
     `collect` : 스트림의 요소를 수집하여 새로운 컬렉션을 반환<br>
     `reduce` : 스트림의 요소를 하나로 줄여서 반환<br>
     `count` : 스트림의 요소 개수를 반환<br>
     `anyMatch` : 주어진 조건에 맞는 요소가 하나라도 있는지 확인
     </p>
     */
    public List<ReviewDTO> getAllReview(int productId) {
        List<Review> reviews = reviewMapper.selectAllReview(productId);
        // stream 생성
        return reviews.stream()
                // 중간 연산 : Review -> ReviewDTO로 변환
                .map(Review::toDTO)
                // 최종 연산 : List<ReviewDTO>로 변환
                .collect(Collectors.toList());
    }

    /**
     주어진 리뷰 ID에 해당하는 리뷰를 조회하여 ReviewDTO로 변환하여 반환합니다.

     @param reviewId 조회할 리뷰의 ID
     @return 리뷰 ID에 해당하는 ReviewDTO 객체, 리뷰가 존재하지 않으면 null 반환
     */
    public ReviewDTO getReview(int reviewId) {
        // 리뷰 ID에 해당하는 리뷰를 데이터베이스에서 조회
        Review review = reviewMapper.selectReview(reviewId);
        // 리뷰가 존재하는 경우 Review 객체를 ReviewDTO로 변환하여 반환, 존재하지 않으면 null 반환
        return review != null ? review.toDTO() : null;
    }

    /**
     주어진 ReviewDTO 객체를 데이터베이스에 삽입합니다.
     if문을 사용하여 이미 존재하는 리뷰인지 확인한 후, 존재하지 않는 경우에만 삽입합니다. (중복 삽입 방지)

     @param reviewDTO 삽입할 리뷰 정보를 담은 ReviewDTO 객체
     */
    public void insertReview(ReviewDTO reviewDTO) {
        if (!reviewMapper.existReview(reviewDTO.getUserId(), reviewDTO.getProductId())) {
            Review review = Review.fromDTO(reviewDTO);
            reviewMapper.insertReview(review);
        }
    }

    /**
     주어진 ReviewDTO 객체를 데이터베이스에 업데이트합니다.

     @param reviewDTO 업데이트할 리뷰 정보를 담은 ReviewDTO 객체
     */
    public void updateReview(ReviewDTO reviewDTO) {
        // 업데이트 요청에서 이미지가 없으면 이미지 컬럼 기존 데이터 유지
        Review review = Review.fromDTO(reviewDTO);
        if (reviewDTO.getReviewImg() == null) {
            review.setReviewImg(reviewMapper.selectReview(reviewDTO.getReviewId()).getReviewImg());
        }
        reviewMapper.updateReview(review);
    }

    /**
     주어진 리뷰 ID에 해당하는 리뷰를 데이터베이스에서 삭제합니다.

     @param reviewId 삭제할 리뷰의 ID
     */
    public void deleteReview(int reviewId) {
        reviewMapper.deleteReview(reviewId);
    }

    /**
     주어진 리뷰 ID에 해당하는 리뷰의 좋아요 수를 1 증가시킵니다.

     @param reviewId 좋아요 수를 증가시킬 리뷰의 ID
     */
    public void increaseLikeCount(int reviewId) {
        reviewMapper.increaseLikeCount(reviewId);
    }

    /**
     주어진 리뷰 ID에 해당하는 리뷰의 좋아요 수를 1 감소시킵니다.

     @param reviewId 좋아요 수를 감소시킬 리뷰의 ID
     */
    public void decreaseLikeCount(int reviewId) {
        reviewMapper.decreaseLikeCount(reviewId);
    }

    /**
     주어진 사용자 ID와 상품 ID에 해당하는 리뷰가 존재하는지 확인합니다.

     @param userId 사용자 ID
     @param productId 상품 ID
     @return 리뷰가 존재하는지 여부
     */
    public boolean existReview(String userId, int productId) {
        return reviewMapper.existReview(userId, productId);
    }

}
