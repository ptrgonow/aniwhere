package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.domain.OrderHistory;
import com.aniwhere.domain.shop.order.dto.OrderDTO;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderPreDTO;
import com.aniwhere.domain.shop.order.dto.OrderTest;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    /*
    @Select("SELECT * FROM orders WHERE order_date BETWEEN #{startDate} AND #{endDate}")
    List<OrderDTO> findOrdersByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT * FROM orders WHERE order_id = #{orderId}")
    OrderDTO findOrderById(@Param("orderId") String orderId);
    */
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
    List<OrderDTO> getOrderItemsByUserId(@Param("userId") String userId, @Param("orderId") String orderId);

    @Insert("INSERT INTO orders (order_id, amount, user_id) VALUES (#{orderId}, #{amount}, #{userId})")
    void insertOrderPre(OrderPreDTO orderPreDTO);

    @Insert("INSERT INTO order_success (order_id, user_id, shipping_address1, shipping_address2, shipping_address3, amount, recipient_email, recipient_name, recipient_phone, order_request) " +
            "VALUES (#{orderId}, #{userId}, #{shippingAddress1},#{shippingAddress2},#{shippingAddress3}, #{amount}, #{recipientEmail}, #{recipientName}, #{recipientPhone}, #{orderRequest}) ")
    void insertOrder(OrderDTO orderDTO);

    @Delete("DELETE FROM orders where order_id = #{orderId}")
    void deleteOrder(OrderDTO orderDTO);

    @Update("UPDATE order_success SET order_status = #{newStatus} WHERE order_id = #{orderId}")
    void updateOrderStatus(@Param("orderId") String orderId, @Param("newStatus") String newStatus);

    @Insert("INSERT INTO order_detail (order_id, product_id, quantity, price) " +
            "VALUES (#{orderId}, #{productId}, #{quantity}, #{price})")
    void insertOrderItem(OrderDetailDTO orderItem);

    @Select("SELECT os.order_id AS orderId, os.amount, os.order_date AS orderDate, os.recipient_name AS recipientName, " +
            "od.product_id AS productId, od.quantity, od.price, p.name " +
            "FROM order_success os " +
            "JOIN order_detail od ON od.order_id = os.order_id " +
            "JOIN product p ON od.product_id = p.product_id " +
            "WHERE os.order_id = #{orderId}")
    List<OrderDTO> getOrderHistory(@Param("orderId") String orderId);

}
