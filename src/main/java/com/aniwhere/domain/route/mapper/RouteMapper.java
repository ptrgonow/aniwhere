package com.aniwhere.domain.route.mapper;


import com.aniwhere.domain.route.domain.Marker;
import com.aniwhere.domain.route.domain.Route;
import com.aniwhere.domain.route.dto.MarkerDTO;
import com.aniwhere.domain.route.dto.RouteDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RouteMapper {


    @Insert("INSERT INTO route (name, description, created_at, user_id) VALUES (#{name}, #{description}, #{createdAt}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertRoute(Route route);

    @Select("SELECT id, name, description, created_at, user_id FROM route WHERE id = #{id}")
    RouteDTO selectRouteById(@Param("id") Long id);

    @Select("SELECT id, route_id, longitude, latitude FROM marker WHERE route_id = #{id}")
    List<MarkerDTO> selectMarkersByRouteId(@Param("id") Long id);

    @Insert("INSERT INTO marker (route_id, longitude, latitude) VALUES (#{routeId}, #{longitude}, #{latitude})")
    void insertMarker(Marker marker);

    @Update("UPDATE route SET name = #{routeDto.name}, description = #{routeDto.description} WHERE id = #{id}")
    void updateRoute(@Param("id") Long id, @Param("routeDto") RouteDTO routeDto);

    @Delete("DELETE FROM marker WHERE route_id = #{routeId}")
    void deleteMarkersByRouteId(@Param("routeId") Long routeId);

}

