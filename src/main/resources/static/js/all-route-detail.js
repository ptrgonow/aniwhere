$(document).ready(function() {
    getMapboxAccessToken();
    $('#allRouteList').on('click', function (){
        window.location.href = '/route/list';
    });

    // 페이지가 로드될 때 마커 데이터를 로드
    const routeId = $('#routeId').val();
    if (routeId) {
        loadMarkers(routeId);
    }

});

let mapboxAccessToken = 'none';
let map = null;
let markers = [];
let lines = [];
let markerId = 0;
let lineId = 0;
let previousSegmentCount = 0;
let currentLocationMarker = null;
const markerRadius = 30;
const colors = ['#FFB3BA', '#FFDFBA', '#FFFFBA', '#BAFFC9', '#BAE1FF'];

function getMapboxAccessToken() {
    $.ajax({
        type: 'GET',
        url: '/getMapboxAccessToken',
        success: function(data) {
            mapboxAccessToken = data.token;
            initializeMap();
        },
        error: function(data) {
            console.error("Error getting Mapbox access token:", data);
        }
    });
}

function initializeMap() {
    mapboxgl.accessToken = mapboxAccessToken;

    const currentHour = new Date().getHours();
    const mapStyle = (currentHour >= 20 || currentHour < 8) ? 'mapbox://styles/mapbox/dark-v11' : 'mapbox://styles/mapbox/light-v11';

    navigator.geolocation.getCurrentPosition((position) => {
        map = new mapboxgl.Map({
            container: 'map',
            style: mapStyle,
            center: [position.coords.longitude, position.coords.latitude],
            zoom: 16,
            language: 'ko-KR',
            preserveDrawingBuffer: true,
            draggable: false
        });

        map.on('load', () => {
            map.loadImage('/img/fIcon.png', (error, image) => {
                if (error) throw error;
                map.addImage('fIcon', image);
                addCurrentLocationMarker(position.coords.longitude, position.coords.latitude);
                // 마커 로드 위치를 지도 초기화 이후로 이동
                const routeId = $('#routeId').val();
                if (routeId) {
                    loadMarkers(routeId);
                }
            });
        });

        // 마커 추가 이벤트 제거
        map.off('click');
    }, (error) => {
        console.error("Error getting user's current location:", error);
    });
}

function addCurrentLocationMarker(longitude, latitude) {
    let img = document.createElement('img');
    img.src = '/img/Icon.png';
    img.style.width = '50px';
    img.style.height = '50px';

    currentLocationMarker = new mapboxgl.Marker({ element: img, draggable: false })
        .setLngLat([longitude, latitude])
        .addTo(map);
}

function addMarker(longitude, latitude) {
    if (isMarkerExist(longitude, latitude)) {
        console.log("기존 마커가 있는 위치입니다. 새로운 마커를 생성하지 않습니다.");
        return;
    }

    let markerIdStr = 'marker' + markerId++;

    let img = document.createElement('img');
    img.src = '/img/fIcon.png';
    img.style.width = '30px';
    img.style.height = '30px';

    let marker = new mapboxgl.Marker({ element: img, draggable: false })
        .setLngLat([longitude, latitude])
        .addTo(map);

    markers.push({ id: markerIdStr, marker: marker, coordinates: [longitude, latitude] });

    const popup = new mapboxgl.Popup({ offset: 25, closeOnClick: false })
        .setText(`구간 ${markerId}`)
        .setMaxWidth("none")
        .addTo(map);
    marker.setPopup(popup);

    // 드래그 시작 시 경고창 띄우고 드래그를 방지
    marker.on('dragstart', (event) => {
        alert("마커를 수정할 수 없습니다.");
        event.preventDefault();
        event.stopPropagation();
        marker.setLngLat([longitude, latitude]); // 원래 위치로 되돌림
    });

    updateLines();
    calDnT();
}

function isMarkerExist(longitude, latitude) {
    return markers.some(markerInfo => {
        const markerLngLat = markerInfo.marker.getLngLat();
        const distance = calculateDistance(longitude, latitude, markerLngLat.lng, markerLngLat.lat);
        return distance < markerRadius;
    });
}

function calculateDistance(lng1, lat1, lng2, lat2) {
    return map.project([lng1, lat1]).dist(map.project([lng2, lat2]));
}

function drawLine(coordinates, lineId, color) {
    if (map.getSource(lineId)) {
        map.getSource(lineId).setData({
            'type': 'Feature',
            'geometry': {
                'type': 'LineString',
                'coordinates': coordinates
            }
        });
    } else {
        map.addSource(lineId, {
            'type': 'geojson',
            'data': {
                'type': 'Feature',
                'geometry': {
                    'type': 'LineString',
                    'coordinates': coordinates
                }
            }
        });

        map.addLayer({
            'id': lineId + '-outline',
            'type': 'line',
            'source': lineId,
            'layout': {
                'line-join': 'round',
                'line-cap': 'round'
            },
            'paint': {
                'line-color': 'rgba(0,0,0,0.99)',
                'line-width': 6
            }
        });

        map.addLayer({
            'id': lineId,
            'type': 'line',
            'source': lineId,
            'layout': {
                'line-join': 'round',
                'line-cap': 'round'
            },
            'paint': {
                'line-color': color,
                'line-width': 4
            }
        });

        lines.push(lineId);
    }
}


function updateLines() {
    for (let i = 0; i < markers.length - 1; i++) {
        let coordinates = [markers[i].coordinates, markers[i + 1].coordinates];
        let lineId = 'line' + i;
        let color = colors[i % colors.length];
        drawLine(coordinates, lineId, color);
    }
    calDnT();
}

function calDnT() {
    if (markers.length < 2) {
        $('.total-d').text('');
        $('.directions-info').html('');
        previousSegmentCount = 0;
        return;
    }

    let totalDistance = 0;
    let segments = [];

    for (let i = 1; i < markers.length; i++) {
        const [lng1, lat1] = markers[i - 1].coordinates;
        const [lng2, lat2] = markers[i].coordinates;

        const R = 6371e3;
        const φ1 = lat1 * Math.PI / 180;
        const φ2 = lat2 * Math.PI / 180;
        const Δφ = (lat2 - lat1) * Math.PI / 180;
        const Δλ = (lng2 - lng1) * Math.PI / 180;

        const a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
            Math.cos(φ1) * Math.cos(φ2) *
            Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        const distance = R * c;
        totalDistance += distance;
        segments.push(distance);
    }

    const averageSpeed = 5 * 1000 / 3600;
    const totalTime = totalDistance / averageSpeed;

    const formatDistance = (distance) => {
        if (distance < 1000) {
            return `${distance.toFixed(2)} m`;
        } else {
            return `${(distance / 1000).toFixed(2)} km`;
        }
    };

    const formatTime = (timeInSeconds) => {
        const hours = Math.floor(timeInSeconds / 3600);
        const minutes = Math.floor((timeInSeconds % 3600) / 60);
        const seconds = Math.floor(timeInSeconds % 60);

        if (hours > 0) {
            return `${hours}시간 ${minutes}분 ${seconds}초`;
        } else {
            return `${minutes}분 ${seconds}초`;
        }
    };

    $('.directions-info').html('');

    segments.forEach((distance, index) => {
        const time = distance / averageSpeed;
        const segmentElement = $('<div></div>').addClass('segment-info').css('backgroundColor', colors[index % colors.length]);

        const segmentText = $('<div></div>').html(`경로 ${index + 1} : ${formatDistance(distance)} <br>소요시간 : ${formatTime(time)}`);

        segmentElement.append(segmentText);

        $('.directions-info').append(segmentElement);
    });

    previousSegmentCount = segments.length;

    $('.total-d').html(`
        총 거리 : ${formatDistance(totalDistance)}<br>
        총 소요시간 : ${formatTime(totalTime)}<br>
    `);
}

function loadMarkers(id) {
    $.ajax({
        type: 'GET',
        url: `/mypage/one/routedetail/${id}`,
        success: function(data) {
            console.log("Loaded markers:", data);

            if (data && data.markers) {
                data.markers.forEach(marker => {
                    console.log("Adding marker:", marker);
                    addMarker(marker.longitude, marker.latitude);
                });
                const center = calculateCenter(data.markers);
                map.setCenter(center);
                map.setZoom(15); // 줌 레벨 설정
            }
        },
        error: function(data) {
            console.error("Error loading markers:", data);
        }
    });
}


// 마커 위치
function calculateCenter(markers) {
    if (markers.length === 0) return [0, 0];

    let sumLng = 0, sumLat = 0;
    markers.forEach(marker => {
        sumLng += marker.longitude;
        sumLat += marker.latitude;
    });

    return [sumLng / markers.length, sumLat / markers.length];
}
