package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.domain.OrderHistory;
import com.aniwhere.domain.shop.order.dto.OrderDTO;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderPreDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderMapper orderMapper;
    private HomeService homeService;

   /* public List<OrderDTO> findOrdersByDateRange(String startDate, String endDate) {
        return orderMapper.findOrdersByDateRange(startDate, endDate);
    }

    public OrderDTO findOrderById(String orderId) {
        return orderMapper.findOrderById(orderId);
    }*/

    public List<Cart> getCheckedCartItems(String userId) {
        return orderMapper.getCheckedCartItemsByUserId(userId);
    }

    @Transactional
    public void saveOrder(OrderPreDTO orderPreDTO, List<OrderDetailDTO> products) {

        String userId = homeService.getAuthenticatedUserId();

        orderPreDTO.setUserId(userId);
        orderMapper.insertOrderPre(orderPreDTO);

        for(OrderDetailDTO product : products){
            orderMapper.insertOrderItem(product);
        }
    }
}
