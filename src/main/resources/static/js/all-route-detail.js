$(document).ready(function (){
   getMapboxAccessToken();
});

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
            _draggable: false
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

        map.on('click', (event) => {
            addMarker(event.lngLat.lng, event.lngLat.lat);
        });

    }, (error) => {
        console.error("Error getting user's current location:", error);
    });
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

    calDnT();
}