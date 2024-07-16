package com.aniwhere.domain.admin.mapper;

import com.aniwhere.domain.admin.dto.MailDTO;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.user.join.dto.JoinDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("SELECT id, user_id as userId, email, user_name as userName, address, detail_address as detailAddress, " +
            "zip_code as zipCode, phone, created_at as createdAt from user order by userId")
    public List<JoinDTO> selectAllUsers();

    @Insert("INSERT INTO user_mail (title, content) VALUES (#{title}, #{content})")
    void insertMail(MailDTO mailDTO);

        @Select("SELECT email FROM user")
        List<String> selectAllUserEmails();


    @Select("SELECT order_id as orderId, user_id as userId, shipping_address1 as shippingAddress1, " +
            "shipping_address2 as shippingAddress2, shipping_address3 as shippingAddress3, " +
            "amount, order_status as orderStatus, order_date as orderDate, recipient_name as recipientName, " +
            "recipient_phone as recipientPhone, order_request as orderRequest from order_success order by" +
            " order_date desc LIMIT #{limit} OFFSET #{offset}")
    List<OrderSucDTO> selectAllOrders(@Param("limit") int limit, @Param("offset") int offset);

    @Select("SELECT os.order_id AS orderId, " +
            "       os.user_id AS userId, " +
            "       u.user_name as userName, " +
            "       u.is_social as isSocial, " +
            "       os.shipping_address1 AS shippingAddress1, " +
            "       os.shipping_address2 AS shippingAddress2, " +
            "       os.shipping_address3 AS shippingAddress3, " +
            "       os.amount, os.order_status AS orderStatus, os.order_date AS orderDate, os.recipient_name AS recipientName, " +
            "       os.recipient_phone AS recipientPhone, os.order_request AS orderRequest " +
            "FROM order_success os " +
            "JOIN user u ON os.user_id = u.user_id " +
            "WHERE os.order_id = #{orderId} " +
            "ORDER BY os.payment_key DESC " +
            "LIMIT 1;")
    OrderSucDTO selectOrderById(@Param("orderId") String orderId);

    @Select("SELECT COUNT(*) FROM order_success")
    int countOrders();

    @Select("SELECT od.product_id AS productId, od.quantity, p.price, p.name, p.image " +
            "FROM order_detail od " +
            "JOIN product p ON od.product_id = p.product_id " +
            "WHERE od.order_id = #{orderId}")
    List<OrderDetailDTO> selectOrderDetailByOrderId(@Param("orderId") String orderId);

}
