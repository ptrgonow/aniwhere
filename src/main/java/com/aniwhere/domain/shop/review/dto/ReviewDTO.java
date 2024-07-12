package com.aniwhere.domain.shop.review.dto;

import lombok.Data;

/**
    <p>
    `@Data` 어노테이션 :
    <br>
    해당 클래스의 모든 필드에 대한 getter, setter, equals, hashCode, toString 메서드를 생성해준다.
    Lombok 라이브러리의 어노테이션 중 하나이다.
    </p>
    <br>
    <p>
    DTO(Data Transfer Object) :
    <br>
    계층간 데이터 교환을 위한 자바빈즈(JavaBeans)의 일종으로, 데이터베이스 레코드를 객체로 변환한 것이다.
    DTO는 로직을 갖고 있지 않는 순수한 데이터 객체이며, getter/setter 메서드를 가진다.
    </p>
 */
@Data
public class ReviewDTO {

    private int reviewId;
    private int productId;
    private String userId;
    private String reviewContent;
    private int reviewRating;
    private String reviewImg;
    private int reviewLike;
}
