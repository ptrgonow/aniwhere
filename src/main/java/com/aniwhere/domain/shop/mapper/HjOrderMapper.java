package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.order.dto.HjOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface HjOrderMapper {

    @Select("SELECT * FROM `order` " +
            "WHERE order_date >= #{startDate} AND order_date <= #{endDate}")
    List<HjOrderDTO> findOrderByDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT o.order_id, o.user_id, o.shipping_address1, o.shipping_address2, o.shipping_address3, " +
            "o.amount, o.order_status, o.order_date, o.recipient_name, o.recipient_email, o.recipient_phone, " +
            "o.order_request, o.payment_type, o.payment_key, p.image AS product_image, " +
            "od.quantity, od.price " +
            "FROM `order` o " +
            "JOIN order_detail od ON o.order_id = od.order_id " +
            "JOIN product p ON od.product_id = p.product_id " +
            "WHERE o.order_date BETWEEN #{startDate} AND #{endDate}")
    List<HjOrderDTO> findImagesByDates(@Param("startDate") String startDate, @Param("endDate") String endDate);

}

