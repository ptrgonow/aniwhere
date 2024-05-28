package com.aniwhere.domain.route.service;

import com.aniwhere.domain.route.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**

    RouteService 클래스는 비즈니스 로직을 처리하는 서비스 계층을 담당합니다.
    이 클래스는 RouteMapper 를 통해 데이터베이스와 상호작용합니다.

 */
@Service
public class RouteService {

    // 데이터 접근 계층과의 상호작용을 위한 RouteMapper 변수 선언
    private final RouteMapper routeMapper;

    /**

        RouteService 생성자. RouteMapper 를 의존성 주입받아 초기화합니다.

        @param routeMapper 데이터 접근을 위한 RouteMapper 객체

     */
    @Autowired
    public RouteService(RouteMapper routeMapper) {
        // 의존성 주입을 통해 RouteMapper 초기화
        this.routeMapper = routeMapper;
    }

}
