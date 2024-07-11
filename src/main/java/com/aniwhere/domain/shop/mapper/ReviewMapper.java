package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.review.domain.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
     <p>
     `@Mapper` 어노테이션 :
     <br>
     MyBatis의 매퍼 인터페이스를 선언하는 인터페이스에 사용하는 어노테이션.
     이 어노테이션을 사용하면 스프링이 해당 인터페이스를 스캔하여 MyBatis의 매퍼로 등록한다. (mapperScan 어노테이션을 사용하지 않아도 된다.)
     </p>
     <br>
     <p>
     매퍼 인터페이스 :
     <br>
     데이터 베이스에 접근하기 위한 메서드를 선언하는 인터페이스.
     각 메서드는 매퍼 XML 파일에 정의된 SQL 문을 호출하는데 사용하거나 직접 @Select, @Insert, @Update, @Delete 어노테이션을 사용하여 SQL 문을 직접 정의할 수 있다.
     </p>
 */
@Mapper
public interface ReviewMapper {

    @Select("SELECT COUNT(*) FROM review WHERE user_id = #{userId} AND product_id = #{productId}")
    boolean existReview(String userId, int productId);

    @Select("SELECT review_id AS reviewId, user_id AS userId, product_id AS productId, review_content AS reviewContent, review_rating AS reviewRating, review_img AS reviewImg, review_like AS reviewLike, created_at FROM review")
    List<Review> selectAllReview( );

    @Select("SELECT review_id AS reviewId, user_id AS userId, product_id AS productId, review_content AS reviewContent, review_rating AS reviewStar, review_img AS reviewImg, review_like AS reviewLike, created_at AS reviewCreatedAt, updated_at AS reviewUpdatedAt FROM review WHERE review_id = #{reviewId}")
    Review selectReview(int reviewId);

    @Insert("INSERT INTO review (user_id, product_id, review_content, review_rating, review_img, review_like) VALUES (#{userId}, #{productId}, #{reviewContent}, #{reviewStar}, #{reviewImg}, #{reviewLike})")
    void insertReview(Review review);

    @Update("UPDATE review SET review_content = #{reviewContent}, review_rating = #{reviewStar}, review_img = #{reviewImg}, review_like = #{reviewLike} WHERE review_id = #{reviewId}")
    void updateReview(Review review);

    @Delete("DELETE FROM review WHERE review_id = #{reviewId}")
    void deleteReview(int reviewId);

    @Update("UPDATE review SET review_like = review_like + 1 WHERE review_id = #{reviewId}")
    void increaseLikeCount(int reviewId);



}