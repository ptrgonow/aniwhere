package com.aniwhere.domain.shop.order.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderHistoryDTO {

    private OrderPreDTO orderPreDTO;
    private List<OrderDetailDTO> orderItems;
}
