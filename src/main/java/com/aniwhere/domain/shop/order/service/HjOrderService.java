package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.mapper.HjOrderMapper;
import com.aniwhere.domain.shop.order.dto.HjOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HjOrderService {
    private final HjOrderMapper hjOrderMapper;

    public List<HjOrderDTO> getHjOrderListByStartEndDates(String startDate, String endDate) {
        String startDateStr = startDate + " 00:00:00";
        String endDateStr = endDate + " 23:59:59";
        return hjOrderMapper.findOrderByDates(startDateStr, endDateStr);
    }

    public List<HjOrderDTO> getImagesByStartEndDates(String startDate, String endDate) {
        String startDateStr = startDate + " 00:00:00";
        String endDateStr = endDate + " 23:59:59";
        return hjOrderMapper.findImagesByDates(startDateStr, endDateStr);
    }

    // order-details 엔드포인트를 위한 메소드 추가
    public List<HjOrderDTO> getOrderDetailsByOrderId(String orderId) {
        return hjOrderMapper.findOrderDetailsByOrderId(orderId);
    }
}
