package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderHistoryDTO {

    private OrderDTO orderDTO;
    private List<OrderDetailDTO> orderItems;
}
