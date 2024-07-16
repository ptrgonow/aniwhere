package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderPreDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderMapper orderMapper;
    private HomeService homeService;

   /* public List<OrderSucDTO> findOrdersByDateRange(String startDate, String endDate) {
        return orderMapper.findOrdersByDateRange(startDate, endDate);
    }

    public OrderSucDTO findOrderById(String orderId) {
        return orderMapper.findOrderById(orderId);
    }*/

    public List<Cart> getCheckedCartItems(String userId) {
        return orderMapper.getCheckedCartItemsByUserId(userId);
    }

    public List<OrderSucDTO> getOrderItemsById(String userId, String orderId){
        return orderMapper.getOrderItemsByUserId(userId, orderId);
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
    @Transactional
    public void makeOrder(OrderSucDTO orderSucDTO) {

        String userId = homeService.getAuthenticatedUserId();

        orderSucDTO.setUserId(userId);
        orderMapper.insertOrder(orderSucDTO);
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        orderMapper.updateOrderStatus(orderId, newStatus);
    }

    public void deleteFromCart(String userId){
        orderMapper.deleteFromCart(userId);
    }


}
