package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.dto.OrderDTO;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM orders WHERE order_date BETWEEN #{startDate} AND #{endDate}")
    List<OrderDTO> findOrdersByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT * FROM orders WHERE order_id = #{orderId}")
    OrderDTO findOrderById(@Param("orderId") String orderId);

    @Select("SELECT user_id AS userId, user_pwd AS userPwd, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone " +
            "FROM user WHERE user_id = #{userId}")
    UserDetailDTO detailByUserId(@Param("userId") String userId);

    @Select("SELECT c.cart_id as cartId, c.quantity, p.product_id as productId, c.user_id as userId, p.name, p.image, p.price, c.total_price as totalPrice " +
            "FROM cart c JOIN product p ON c.product_id = p.product_id " +
            "WHERE c.user_id = #{userId} AND c.checked = 'Y'")
    List<Cart> getCheckedCartItemsByUserId(String userId);

}
