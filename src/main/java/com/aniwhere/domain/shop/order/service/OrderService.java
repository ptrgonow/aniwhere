package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.dto.OrderSearchDTO;
import com.aniwhere.domain.shop.order.dto.OrderSucDTO;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.shop.order.dto.OrderPreDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderMapper orderMapper;
    private HomeService homeService;

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

        // 기존 주문 삭제
        if (!orderMapper.getOrderItemsByUserId(userId, orderSucDTO.getOrderId()).isEmpty()) {
            orderMapper.deleteOrderSuccess(orderSucDTO.getOrderId());
        }

        // order_id 확인
        System.out.println("Order ID before insertion: " + orderSucDTO.getOrderId());

        // orders 테이블에 주문 삽입
        orderSucDTO.setUserId(userId);
        orderMapper.insertOrder(orderSucDTO);
        // order_id 확인
        System.out.println("Order ID after insertion: " + orderSucDTO.getOrderId());

        // 장바구니에서 주문한 상품 삭제
        orderMapper.deleteFromCart(userId);
    }


    @Transactional
    public void updateOrderStatus(String orderId, String newStatus) {
        orderMapper.updateOrderStatus(orderId, newStatus);
        orderMapper.updateQuantity(orderId);
    }

    public void deleteFromCart(String userId){
        orderMapper.deleteFromCart(userId);
    }

    public List<OrderSearchDTO> getOrderItemsByUserIdForDate(String userId, String startDate, String endDate) {
        return orderMapper.getOrderItemsByUserIdForDate(userId, startDate, endDate);
    }

}
