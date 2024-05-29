package com.aniwhere.domain.route.mapper;

import com.aniwhere.domain.route.domain.Marker;
import com.aniwhere.domain.route.domain.Route;
import com.aniwhere.domain.route.dto.MarkerDTO;
import com.aniwhere.domain.route.dto.RouteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RouteMapper {

    void insertRoute(Route route);
    RouteDTO selectRouteById(Long id);
    List<MarkerDTO> selectMarkersByRouteId(Long id);
    void insertMarker(Marker marker);
    void updateRoute(@Param("id") Long id, @Param("routeDto") RouteDTO routeDto);
    void deleteMarkersByRouteId(Long routeId);
}
