package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.cart.domain.Cart;
import com.aniwhere.domain.shop.order.domain.OrderHistory;
import com.aniwhere.domain.shop.order.dto.OrderDTO;
import com.aniwhere.domain.shop.mapper.OrderMapper;
import com.aniwhere.domain.shop.order.dto.OrderDetailDTO;
import com.aniwhere.domain.user.loginSession.service.HomeService;
import com.aniwhere.domain.user.mypage.dto.UserDetailDTO;
import lombok.AllArgsConstructor;

import org.apache.ibatis.annotations.Insert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderMapper orderMapper;
    private HomeService homeService;

    private String getAuthenticatedUserId() {

        return homeService.getAuthenticatedUserId();
    }
  
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

    @Transactional
    public void saveOrder(OrderDTO orderDTO, List<OrderDetailDTO> products) {

        String userId = getAuthenticatedUserId();

        orderDTO.setUserId(userId);
        orderMapper.insertOrder(orderDTO);

        for(OrderDetailDTO product : products){
            orderMapper.insertOrderItem(product);
        }
    }
}
