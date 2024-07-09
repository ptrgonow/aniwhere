package com.aniwhere.domain.route.mapper;


import com.aniwhere.domain.route.domain.Marker;
import com.aniwhere.domain.route.domain.Route;
import com.aniwhere.domain.route.dto.MarkerDTO;
import com.aniwhere.domain.route.dto.RouteDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RouteMapper {

    @Select("SELECT COUNT(*) FROM route")
    int countRoutes();

    @Select("SELECT r.id, r.name, r.description, r.created_at AS createdAt, u.user_name AS userName, r.image" +
            "        FROM route r" +
            "        JOIN user u ON r.user_id = u.id" +
            "        ORDER BY r.created_at DESC" +
            "        LIMIT #{limit} OFFSET #{offset}")
    List<RouteDTO> selectRoutes(@Param("limit") int limit, @Param("offset") int offset);

    @Insert("INSERT INTO route (name, description, created_at, user_id, image) VALUES (#{name}, #{description}, #{createdAt}, #{userId}, #{image})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertRoute(Route route);

    @Select("SELECT id, name, description, created_at AS createdAt, user_id AS userId, image FROM route WHERE id = #{id}")
    RouteDTO selectRouteById(@Param("id") Long id);

    @Select("SELECT id, route_id, longitude, latitude FROM marker WHERE route_id = #{id}")
    List<MarkerDTO> selectMarkersByRouteId(@Param("id") Long id);

    @Insert("INSERT INTO marker (route_id, longitude, latitude) VALUES (#{routeId}, #{longitude}, #{latitude})")
    void insertMarker(Marker marker);

    @Update("UPDATE route SET name = #{routeDto.name}, description = #{routeDto.description}, image = #{routeDto.image} WHERE id = #{id}")
    void updateRoute(@Param("id") Long id, @Param("routeDto") RouteDTO routeDto);

    @Delete("DELETE FROM marker WHERE route_id = #{routeId}")
    void deleteMarkersByRouteId(@Param("routeId") Long routeId);



}

