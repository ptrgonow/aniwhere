package com.aniwhere.domain.shop.order.service;

import com.aniwhere.domain.shop.mapper.HjOrderMapper;
import com.aniwhere.domain.shop.order.dto.HjOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HjOrderService {
    private final HjOrderMapper hjOrderMapper;

    public List<HjOrderDTO> getHjOrderListByStartEndDates(String startDate, String endDate) {
        String startDateStr = startDate + " 00:00:00";
        String endDateStr = endDate + " 00:00:00";
        System.out.println(startDateStr + " sql " + endDateStr);
        return hjOrderMapper.findOrderByDates(startDateStr, endDateStr);
    }
}
