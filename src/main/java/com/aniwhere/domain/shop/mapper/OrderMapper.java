package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.dto.OrderSearchDTO;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderPreDTO;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT user_id AS userId, user_pwd AS userPwd, user_name AS userName, email, address, detail_address AS detailAddress, zip_code AS zipCode, phone " +
            "FROM user WHERE user_id = #{userId}")
    UserDetailDTO detailByUserId(@Param("userId") String userId);

    @Select("SELECT c.cart_id as cartId, c.quantity, p.product_id as productId, c.user_id as userId, p.name, p.image, p.price, c.total_price as totalPrice " +
            "FROM cart c JOIN product p ON c.product_id = p.product_id " +
            "WHERE c.user_id = #{userId} AND c.checked = 'Y'")
    List<Cart> getCheckedCartItemsByUserId(String userId);

    @Select("SELECT op.order_id as orderId, op.amount, op.user_id as userId, od.product_id as prodcutId, od.quantity, od.price, " +
            "p.name " +
            "FROM orders op JOIN order_detail od ON op.order_id = od.order_id join product p on od.product_id = p.product_id " +
            "WHERE op.user_id = #{userId} AND od.order_id = #{orderId}")
    List<OrderSucDTO> getOrderItemsByUserId(@Param("userId") String userId, @Param("orderId") String orderId);

    @Insert("INSERT INTO orders (order_id, amount, user_id) VALUES (#{orderId}, #{amount}, #{userId})")
    void insertOrderPre(OrderPreDTO orderPreDTO);

    @Insert("INSERT INTO order_success (order_id, user_id, shipping_address1, shipping_address2, shipping_address3, amount, recipient_email, recipient_name, recipient_phone, order_request) " +
            "VALUES (#{orderId}, #{userId}, #{shippingAddress1},#{shippingAddress2},#{shippingAddress3}, #{amount}, #{recipientEmail}, #{recipientName}, #{recipientPhone}, #{orderRequest}) ")
    void insertOrder(OrderSucDTO orderSucDTO);

    @Delete("DELETE FROM orders where order_id = #{orderId}")
    void deleteOrder(OrderSucDTO orderSucDTO);

    @Update("UPDATE order_success SET order_status = #{newStatus} WHERE order_id = #{orderId}")
    void updateOrderStatus(@Param("orderId") String orderId, @Param("newStatus") String newStatus);

    @Insert("INSERT INTO order_detail (order_id, product_id, quantity, price) " +
            "VALUES (#{orderId}, #{productId}, #{quantity}, #{price})")
    void insertOrderItem(OrderDetailDTO orderItem);

    @Delete("DELETE from cart where user_id = #{userId}")
    void deleteFromCart(@Param("userId") String userId);

    @Select("SELECT os.order_id AS orderId, " +
            "os.user_id AS userId, " +
            "od.product_id AS productId, " +
            "od.price, od.quantity, od.price * od.quantity AS totalAmount, " +
            "SUM(od.quantity) AS totalQuantity, " +
            "p.name AS productName, " +
            "p.image AS image, " +
            "os.order_status AS orderStatus, " +
            "os.order_date AS orderDate " +
            "FROM order_success os " +
            "JOIN order_detail od ON os.order_id = od.order_id " +
            "JOIN product p ON od.product_id = p.product_id " +
            "WHERE os.user_id = #{userId} " +
            "AND DATE(os.order_date) BETWEEN #{startDate} AND #{endDate} " +
            "AND os.order_status = '결제 완료' or os.order_status = '배송 출발' or os.order_status = '배송 준비' " +
            "GROUP BY os.order_id, os.user_id, od.product_id, p.name, os.order_status, os.order_date " +
            "ORDER BY os.order_date DESC")
    List<OrderSearchDTO> getOrderItemsByUserIdForDate(@Param("userId") String userId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Update("UPDATE product p\n" +
            "JOIN order_detail od ON p.product_id = od.product_id " +
            "JOIN order_success os ON od.order_id = os.order_id " +
            "SET p.quantity = p.quantity - od.quantity " +
            "WHERE os.order_id = #{orderId};")
    void updateQuantity(String orderId);

    @Delete("DELETE FROM order_success WHERE order_id = #{orderId}")
    void deleteOrderSuccess(String orderId);

}
