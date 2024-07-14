package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.domain.OrderHistory;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {



    /*
    private final OrderMapper orderMapper;


    public List<OrderDTO> findOrdersByDateRange(String startDate, String endDate) {
        return orderMapper.findOrdersByDateRange(startDate, endDate);
    }

    public OrderDTO findOrderById(String orderId) {
        return orderMapper.findOrderById(orderId);
    }
     */

    public UserDetailDTO getUserDetailByUserId(String userId) {

        return orderMapper.detailByUserId(userId);
    }
    public List<Cart> getCheckedCartItems(String userId) {
        return orderMapper.getCheckedCartItemsByUserId(userId);
    }

    @Transactional
    public void saveOrder(String orderId, String userId, String customerEmail, String customerName, String customerMobilePhone, int totalPrice) {

        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderId(orderId);
        orderHistory.setUserId(userId);
        orderHistory.setTotalPrice(totalPrice);
        orderHistory.setOrderStatus("결제 완료");
        orderHistory.setRecipientEmail(customerEmail);
        orderHistory.setRecipientName(customerName);
        orderHistory.setRecipientPhone(customerMobilePhone);
        orderHistory.setOrderRequest("gg");
        orderMapper.insertOrder(orderHistory);

    }


}
