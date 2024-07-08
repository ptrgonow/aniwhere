package com.aniwhere.domain.route.controller;

import com.aniwhere.domain.route.dto.MarkerDTO;
import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class RouteRestController {

    private final RouteService routeService;

    // 경로 생성
    @PostMapping("/route")
    public Long createRoute(@RequestBody RouteDTO routeDto) {
        try {
            return routeService.saveRoute(routeDto);
        } catch (IOException e) {
            System.out.println("Failed to create route" + e.getMessage());
            return -1L;
        }
    }

    // ID로 경로 조회
    @GetMapping("/route/{id}")
    public RouteDTO getRouteById(@PathVariable Long id) {
        return routeService.getRouteById(id);
    }

    // ID로 경로에 포함된 마커 조회
    @GetMapping("/route/{id}/markers")
    public List<MarkerDTO> getMarkersByRouteId(@PathVariable Long id) {
        RouteDTO route = routeService.getRouteById(id);
        return route.getMarkers();
    }

    // 마커 생성
    @PostMapping("/marker")
    public void createMarker(@RequestBody MarkerDTO markerDto) {
        routeService.saveMarker(markerDto);
    }

    // 경로 업데이트
    @PutMapping("/route/{id}")
    public void updateRoute(@PathVariable Long id, @RequestBody RouteDTO routeDto) {
        try {
            routeService.updateRoute(id, routeDto);
        } catch (IOException e) {
            System.out.println("Failed to update route" + e.getMessage());
        }
    }

    // 경로에 포함된 마커 삭제
    @DeleteMapping("/route/{id}/markers")
    public void deleteMarkersByRouteId(@PathVariable Long id) {
        routeService.deleteMarkersByRouteId(id);
    }
}
