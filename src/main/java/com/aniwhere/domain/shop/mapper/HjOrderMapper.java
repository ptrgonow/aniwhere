package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.order.dto.HjOrderDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface HjOrderMapper {

    @Select("SELECT * FROM order_success " +
            "WHERE order_date >= #{startDate} AND order_date <= #{endDate} AND order_status = '결제 완료'")
    List<HjOrderDTO> findOrderByDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT o.order_id, o.user_id, o.shipping_address1, o.shipping_address2, o.shipping_address3, " +
            "o.amount, o.order_status, o.order_date, o.recipient_name, o.recipient_email, o.recipient_phone, " +
            "o.order_request, p.image AS product_image, p.name AS product_name, " +
            "od.quantity, od.price, od.product_id " +
            "FROM order_success o " +
            "JOIN order_detail od ON o.order_id = od.order_id " +
            "JOIN product p ON od.product_id = p.product_id " +
            "WHERE o.order_date BETWEEN #{startDate} AND #{endDate} AND o.order_status = '결제 완료'")
    List<HjOrderDTO> findImagesByDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT product_id, quantity, price " +
            "FROM order_detail " +
            "WHERE order_id = #{orderId}")
    List<HjOrderDTO> findOrderDetailsByOrderId(@Param("orderId") String orderId);
}

