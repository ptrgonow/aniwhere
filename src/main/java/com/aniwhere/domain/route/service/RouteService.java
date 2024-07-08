package com.aniwhere.domain.route.service;

import com.aniwhere.domain.route.domain.Marker;
import com.aniwhere.domain.route.domain.Route;
import com.aniwhere.domain.route.dto.MarkerDTO;
import com.aniwhere.domain.route.dto.RouteDTO;
import com.aniwhere.domain.route.mapper.RouteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteMapper routeMapper;

    public RouteDTO getRouteById(Long id) {
        RouteDTO route = routeMapper.selectRouteById(id);
        List<MarkerDTO> markers = routeMapper.selectMarkersByRouteId(id);
        route.setMarkers(markers);
        return route;
    }

    public Long saveRoute(RouteDTO routeDto) throws IOException {
        Route route = new Route();
        route.setName(routeDto.getName());
        route.setDescription(routeDto.getDescription());
        route.setCreatedAt(LocalDateTime.now());
        routeMapper.insertRoute(route);

        List<Marker> markers = routeDto.getMarkers().stream()
                .map(markerDto -> {
                    Marker marker = new Marker();
                    marker.setRouteId(route.getId());
                    marker.setLongitude(markerDto.getLongitude());
                    marker.setLatitude(markerDto.getLatitude());
                    return marker;
                }).collect(Collectors.toList());

        saveMarkers(route.getId(), markers);

        // 이미지 저장
        if (routeDto.getImage() != null && !routeDto.getImage().isEmpty()) {
            saveImage(routeDto.getImage(), route.getId());
        }

        return route.getId();
    }

    public void saveMarkers(Long routeId, List<Marker> markers) {
        markers.forEach(marker -> {
            marker.setRouteId(routeId);
            routeMapper.insertMarker(marker);
        });
    }

    public void updateRoute(Long id, RouteDTO routeDto) throws IOException {
        // 경로 업데이트
        routeMapper.updateRoute(id, routeDto);

        // 기존 마커 삭제 및 새로운 마커 저장
        deleteMarkersByRouteId(id);
        List<Marker> markers = routeDto.getMarkers().stream()
                .map(markerDto -> {
                    Marker marker = new Marker();
                    marker.setRouteId(id);
                    marker.setLongitude(markerDto.getLongitude());
                    marker.setLatitude(markerDto.getLatitude());
                    return marker;
                }).collect(Collectors.toList());

        saveMarkers(id, markers);

        // 이미지 저장
        if (routeDto.getImage() != null && !routeDto.getImage().isEmpty()) {
            saveImage(routeDto.getImage(), id);
        }
    }

    public void deleteMarkersByRouteId(Long routeId) {
        routeMapper.deleteMarkersByRouteId(routeId);
    }

    public void saveMarker(MarkerDTO markerDto) {
        Marker marker = new Marker();
        marker.setRouteId(markerDto.getRouteId());
        marker.setLongitude(markerDto.getLongitude());
        marker.setLatitude(markerDto.getLatitude());
        routeMapper.insertMarker(marker);
    }

    // 이미지 저장 메서드
    private void saveImage(String base64Image, Long routeId) throws IOException, FileNotFoundException {
        String[] parts = base64Image.split(",");
        String imageString = parts[1];
        byte[] imageBytes = Base64.getDecoder().decode(imageString);
        String timeStamp = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
        String fileName = "src/main/resources/static/images/" + routeId + "_" + timeStamp + ".png";

        Files.createDirectories(Paths.get("src/main/resources/static/images/"));

        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            outputStream.write(imageBytes);
        }
    }
}
