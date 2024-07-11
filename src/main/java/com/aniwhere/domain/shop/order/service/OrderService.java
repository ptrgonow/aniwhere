package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.dto.OrderDTO;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public List<OrderDTO> findOrdersByDateRange(String startDate, String endDate) {
        return orderMapper.findOrdersByDateRange(startDate, endDate);
    }

    public OrderDTO findOrderById(String orderId) {
        return orderMapper.findOrderById(orderId);
    }

    public UserDetailDTO getUserDetailByUserId(String userId) {

        return orderMapper.detailByUserId(userId);
    }
    public List<Cart> getCheckedCartItems(String userId) {
        return orderMapper.getCheckedCartItemsByUserId(userId);
    }

}
