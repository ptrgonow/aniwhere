package com.aniwhere.domain.shop.review.domain;

import com.aniwhere.domain.shop.review.dto.ReviewDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
    <p>
    Domain Object :
    <br>
    데이터베이스의 테이블에 대응되는 자바 객체로, 비즈니스 로직을 포함할 수 있다.<br>
    주로 DTO와 함께 사용되며, DTO는 Domain Object를 포함할 수 있다.<br>
    DTO는 데이터 전송을 위한 객체이고, Domain Object는 비즈니스 로직을 처리하기 위한 객체이다.<br>
    예시) ReviewDTO, Review(=Domain Object)
    </p>
 */

@Data
public class Review {

    private int reviewId;
    private int productId;
    private String userId;
    private String userName;
    private String reviewContent;
    private int reviewRating;
    private String reviewImg;
    private int reviewLike;
    private LocalDateTime reviewCreatedAt;
    private LocalDateTime reviewUpdatedAt;

    // DTO를 도메인 객체로 변환하는 정적 메서드
    public static Review fromDTO(ReviewDTO dto) {
        Review review = new Review();
        review.setReviewId(dto.getReviewId());
        review.setProductId(dto.getProductId());
        review.setUserId(dto.getUserId());
        review.setUserName(dto.getUserName());
        review.setReviewContent(dto.getReviewContent());
        review.setReviewRating(dto.getReviewRating());
        review.setReviewImg(dto.getReviewImg());
        review.setReviewLike(dto.getReviewLike());
        return review;
    }

    // 도메인 객체를 DTO로 변환하는 메서드
    public ReviewDTO toDTO() {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(this.reviewId);
        dto.setProductId(this.productId);
        dto.setUserId(this.userId);
        dto.setUserName(this.userName);
        dto.setReviewContent(this.reviewContent);
        dto.setReviewRating(this.reviewRating);
        dto.setReviewImg(this.reviewImg);
        dto.setReviewLike(this.reviewLike);
        return dto;
    }
}
