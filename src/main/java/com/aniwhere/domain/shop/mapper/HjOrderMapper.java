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
}
