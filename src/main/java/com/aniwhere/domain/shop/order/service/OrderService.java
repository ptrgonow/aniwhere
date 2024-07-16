package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.dto.OrderDTO;
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

   /* public List<OrderDTO> findOrdersByDateRange(String startDate, String endDate) {
        return orderMapper.findOrdersByDateRange(startDate, endDate);
    }

    public OrderDTO findOrderById(String orderId) {
        return orderMapper.findOrderById(orderId);
    }*/

    public List<Cart> getCheckedCartItems(String userId) {
        return orderMapper.getCheckedCartItemsByUserId(userId);
    }

    public List<OrderDTO> getOrderItemsById(String userId, String orderId){
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
    public void makeOrder(OrderDTO orderDTO) {

        String userId = homeService.getAuthenticatedUserId();

        orderDTO.setUserId(userId);
        orderMapper.insertOrder(orderDTO);
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        orderMapper.updateOrderStatus(orderId, newStatus);
    }

    public void deleteFromCart(String userId){
        orderMapper.deleteFromCart(userId);
    }


}
