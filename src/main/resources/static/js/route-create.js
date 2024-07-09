$(document).ready(function() {
    getMapboxAccessToken();
    $('#createRoute').on('click', captureAndSaveRoute);
    $('#reset').on('click', resetMarkers);
    $('#previous-step').on('click', removeLastMarkerAndLine);

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
            preserveDrawingBuffer: true
        });

        map.on('load', () => {
            map.loadImage('/img/fIcon.png', (error, image) => {
                if (error) throw error;
                map.addImage('fIcon', image);
                addCurrentLocationMarker(position.coords.longitude, position.coords.latitude);
            });
        });

        map.on('click', (event) => {
            addMarker(event.lngLat.lng, event.lngLat.lat);
        });

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

    let marker = new mapboxgl.Marker({ element: img, draggable: true })
        .setLngLat([longitude, latitude])
        .addTo(map);

    markers.push({ id: markerIdStr, marker: marker, coordinates: [longitude, latitude] });

    const popup = new mapboxgl.Popup({ offset: 25, closeOnClick: false })
        .setText(`구간 ${markerId}`)
        .setMaxWidth("none")
        .addTo(map);
    marker.setPopup(popup);

    marker.on('drag', () => {
        let lngLat = marker.getLngLat();
        let markerInfo = markers.find(m => m.id === markerIdStr);
        if (markerInfo) {
            markerInfo.coordinates = [lngLat.lng, lngLat.lat];
        }
        updateLines();
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

function removeLastMarkerAndLine() {
    // 마지막 마커 제거
    if (markers.length > 0) {
        const lastMarker = markers.pop();
        lastMarker.marker.remove();
    }

    // 마지막 경로 제거
    if (lines.length > 0) {
        const lastLineId = lines.pop();
        if (map.getLayer(lastLineId + '-outline')) {
            map.removeLayer(lastLineId + '-outline');
        }
        if (map.getLayer(lastLineId)) {
            map.removeLayer(lastLineId);
        }
        if (map.getSource(lastLineId)) {
            map.removeSource(lastLineId);
        }
    }

    updateLines();
    calDnT();
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

function resetMarkers() {
    markers.forEach(markerInfo => {
        markerInfo.marker.remove();
    });
    markers = [];

    lines.forEach(lineId => {
        if (map.getLayer(lineId + '-outline')) {
            map.removeLayer(lineId + '-outline');
        }
        if (map.getLayer(lineId)) {
            map.removeLayer(lineId);
        }
        if (map.getSource(lineId)) {
            map.removeSource(lineId);
        }
    });

    lines = [];
    markerId = 0;
    lineId = 0;
    $('.total-d').text('');
    $('.directions-info').html('');
}

function captureAndSaveRoute() {
    let mapElement = $('#map');

    if (currentLocationMarker) {
        currentLocationMarker.remove();
    }

    html2canvas(mapElement.get(0), {
        proxy: '/html2canvas/proxy.json',
        allowTaint: false,
        useCORS: true
    }).then((canvas) => {
        let base64Image = canvas.toDataURL("image/png");

        const routeData = {
            userId: $('#userId').val(),
            name: $('#routeName').val(),
            description: $('#routeDescription').val(),
            markers: getMarkersData(),
            image: base64Image
        };

        $.ajax({
            type: 'POST',
            url: '/map/route',
            data: JSON.stringify(routeData),
            contentType: 'application/json',
            success: function(response) {
                alert("경로 데이터와 이미지가 저장되었습니다.");
                window.location.href = '/route/list';
            },
            error: function(data) {
                alert("오류 발생!");
                console.error("Error saving route data and image:", data);
            }
        });

        if (currentLocationMarker) {
            currentLocationMarker.addTo(map);
        }
    });
}

function getMarkersData() {
    return markers.map(markerInfo => ({
        longitude: markerInfo.coordinates[0],
        latitude: markerInfo.coordinates[1]
    }));
}
