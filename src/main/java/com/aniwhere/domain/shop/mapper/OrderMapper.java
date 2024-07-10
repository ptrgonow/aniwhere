package com.aniwhere.domain.shop.mapper;

import com.aniwhere.domain.shop.order.dto.OrderDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM orders WHERE order_date BETWEEN #{startDate} AND #{endDate}")
    List<OrderDTO> findOrdersByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select("SELECT * FROM orders WHERE order_id = #{orderId}")
    OrderDTO findOrderById(@Param("orderId") String orderId);
}
