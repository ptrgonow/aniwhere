package com.aniwhere.domain.shop.order.controller;

import com.aniwhere.domain.shop.order.dto.HjOrderDTO;
import com.aniwhere.domain.shop.order.service.HjOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HjRestController {
    private final HjOrderService hjOrderService;

    @GetMapping("/order-search")
    public Map<String, List<HjOrderDTO>> orderSearchApi(
            @RequestParam(defaultValue = "1990-01-01") String startDate,
            @RequestParam(defaultValue = "2050-01-01") String endDate) {
        System.out.println(startDate + " " + endDate);
        List<HjOrderDTO> hjOrderDTOList
                = hjOrderService.getHjOrderListByStartEndDates(startDate, endDate);

        Map<String, List<HjOrderDTO>> map = new HashMap<>();
        map.put("hjOrderDTOList", hjOrderDTOList);
        return map;
    }

    @GetMapping("/order-images")
    public Map<String, List<HjOrderDTO>> orderImagesApi(
            @RequestParam(defaultValue = "1990-01-01") String startDate,
            @RequestParam(defaultValue = "2050-01-01") String endDate) {
        List<HjOrderDTO> hjOrderDTOList = hjOrderService.getImagesByStartEndDates(startDate, endDate);

        Map<String, List<HjOrderDTO>> map = new HashMap<>();
        map.put("hjOrderDTOList", hjOrderDTOList);
        return map;
    }

    @GetMapping("/order-details")
    public List<HjOrderDTO> orderDetailsApi(@RequestParam String order_id) {
        return hjOrderService.getOrderDetailsByOrderId(order_id);
    }
}
