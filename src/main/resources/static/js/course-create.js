const currentHour = new Date().getHours();
const mapStyle = (currentHour >= 20 || currentHour < 8) ? 'mapbox://styles/mapbox/dark-v11' : 'mapbox://styles/mapbox/light-v11';

function initializeMapbox(token, position) {
    mapboxgl.accessToken = token;
    const map = new mapboxgl.Map({
        container: 'map',
        style: mapStyle,
        center: [position.coords.longitude, position.coords.latitude],
        zoom: 16,
        language: 'ko-KR',
        preserveDrawingBuffer: true
    });
}

function getMapboxAccessToken(callback) {
    $.ajax({
        type: "GET",
        url: "/getMapboxAccessToken",
        success: function (response) {
            callback(response.token); // JSON 응답에서 토큰 추출 후 콜백 호출
        },
        error: function (error) {
            console.error("Error fetching the Mapbox access token:", error);
        }
    });
}

$(document).ready(function () {
    navigator.geolocation.getCurrentPosition(
        function(position) {
            getMapboxAccessToken(function(token) {
                initializeMapbox(token, position);
            });
        },
        function(error) {
            console.error("Error getting the geolocation:", error);
        }
    );
});
