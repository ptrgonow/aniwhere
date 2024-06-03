
// DOMContentLoaded 이벤트가 발생하면 실행
document.addEventListener('DOMContentLoaded', () => {
    let mapInstance = new MapboxMap(); // MapboxMap 인스턴스 생성
    mapInstance.getMapboxAccessToken(); // Mapbox 접근 토큰 가져오기

    const captureButton = document.getElementById('capture');
    const resetButton = document.getElementById('reset');
    const previousStepButton = document.getElementById('previous-step'); // 이전 단계 버튼

    if (captureButton) {
        captureButton.addEventListener('click', () => {
            // 경로 데이터를 가져옵니다.
            var routeData = {
                name: $('#route-name').val(),
                description: $('#route-description').val()
            };

            // 경로 데이터 저장 API 호출
            $.ajax({
                url: 'http://localhost:9955/api/routes',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(routeData),
                success: function (response) {
                    console.log('Route data saved successfully');
                    mapInstance.captureMap(); // 지도 캡처 함수 호출
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.error('Error saving route data: ', jqXHR);
                }
            });
        });
    } else {
        console.error("capture 버튼을 찾을 수 없습니다.");
    }

    if (resetButton) {
        resetButton.addEventListener('click', () => {
            mapInstance.resetMarkers(); // 마커 및 선 초기화 함수 호출
        });
    } else {
        console.error("reset 버튼을 찾을 수 없습니다.");
    }

    if (previousStepButton) {
        previousStepButton.addEventListener('click', () => {
            mapInstance.removeLastMarkerAndLine(); // 마지막 마커와 경로 삭제 함수 호출
        });
    } else {
        console.error("previous-step 버튼을 찾을 수 없습니다.");
    }
});


export class MapboxMap {
    constructor() {
        this.mapboxAccessToken = 'none';
        this.map = null;
        this.markers = [];
        this.lines = [];
        this.markerId = 0;
        this.lineId = 0;
        this.previousSegmentCount = 0;
        this.currentLocationMarker = null;
        this.markerRadius = 30;
        this.totalElement = document.querySelector('.total-d');
        this.directionsInfo = document.querySelector('.directions-info');
        this.colors = ['#FFB3BA', '#FFDFBA', '#FFFFBA', '#BAFFC9', '#BAE1FF'];
    }

    getMapboxAccessToken() {
        $.ajax({
            type: 'GET',
            url: '/getMapboxAccessToken',
            success: (data) => {
                this.mapboxAccessToken = data;
                this.initializeMap();
            },
            error: function (data) {
                console.error("Error getting Mapbox access token:", data);
            }
        });
    }

    initializeMap() {
        mapboxgl.accessToken = this.mapboxAccessToken;

        const currentHour = new Date().getHours();
        const mapStyle = (currentHour >= 20 || currentHour < 8) ? 'mapbox://styles/mapbox/dark-v11' : 'mapbox://styles/mapbox/light-v11';

        navigator.geolocation.getCurrentPosition((position) => {
            this.map = new mapboxgl.Map({
                container: 'map',
                style: mapStyle,
                center: [position.coords.longitude, position.coords.latitude],
                zoom: 16,
                language: 'ko-KR',
                preserveDrawingBuffer: true
            });

            this.map.on('load', () => {
                this.map.loadImage('/images/fIcon.png', (error, image) => {
                    if (error) throw error;
                    this.map.addImage('fIcon', image);
                    this.addCurrentLocationMarker(position.coords.longitude, position.coords.latitude);
                });
            });

            this.map.on('click', (event) => {
                this.addMarker(event.lngLat.lng, event.lngLat.lat);
            });

        }, (error) => {
            console.error("Error getting user's current location:", error);
        });
    }

    addCurrentLocationMarker(longitude, latitude) {
        let img = document.createElement('img');
        img.src = '/images/Icon.png';
        img.style.width = '50px';
        img.style.height = '50px';

        this.currentLocationMarker = new mapboxgl.Marker({ element: img, draggable: false })
            .setLngLat([longitude, latitude])
            .addTo(this.map);
    }

    addMarker(longitude, latitude) {
        if (this.isMarkerExist(longitude, latitude)) {
            console.log("기존 마커가 있는 위치입니다. 새로운 마커를 생성하지 않습니다.");
            return;
        }

        let markerId = 'marker' + this.markerId++;

        let img = document.createElement('img');
        img.src = '/images/fIcon.png';
        img.style.width = '30px';
        img.style.height = '30px';

        let marker = new mapboxgl.Marker({ element: img, draggable: true })
            .setLngLat([longitude, latitude])
            .addTo(this.map);

        this.markers.push({ id: markerId, marker: marker, coordinates: [longitude, latitude] });

        const popup = new mapboxgl.Popup({ offset: 25, closeOnClick: false })
            .setText(`구간 ${this.markerId}`)
            .setMaxWidth("none")
            .addTo(this.map);
        marker.setPopup(popup);

        marker.on('drag', () => {
            let lngLat = marker.getLngLat();
            let markerInfo = this.markers.find(m => m.id === markerId);
            if (markerInfo) {
                markerInfo.coordinates = [lngLat.lng, lngLat.lat];
            }
            this.updateLines();
        });

        this.updateLines();
        this.calDnT();
    }

    isMarkerExist(longitude, latitude) {
        return this.markers.some(markerInfo => {
            const markerLngLat = markerInfo.marker.getLngLat();
            const distance = this.calculateDistance(longitude, latitude, markerLngLat.lng, markerLngLat.lat);
            return distance < this.markerRadius;
        });
    }

    calculateDistance(lng1, lat1, lng2, lat2) {
        return this.map.project([lng1, lat1]).dist(this.map.project([lng2, lat2]));
    }

    drawLine(coordinates, lineId, color) {
        if (this.map.getSource(lineId)) {
            this.map.getSource(lineId).setData({
                'type': 'Feature',
                'geometry': {
                    'type': 'LineString',
                    'coordinates': coordinates
                }
            });
        } else {
            this.map.addSource(lineId, {
                'type': 'geojson',
                'data': {
                    'type': 'Feature',
                    'geometry': {
                        'type': 'LineString',
                        'coordinates': coordinates
                    }
                }
            });

            this.map.addLayer({
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

            this.map.addLayer({
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

            this.lines.push(lineId);
        }
    }

    removeLastMarkerAndLine() {
        // 마지막 마커 제거
        if (this.markers.length > 0) {
            const lastMarker = this.markers.pop();
            lastMarker.marker.remove();
        }

        // 마지막 경로 제거
        if (this.lines.length > 0) {
            const lastLineId = this.lines.pop();
            if (this.map.getLayer(lastLineId + '-outline')) {
                this.map.removeLayer(lastLineId + '-outline');
            }
            if (this.map.getLayer(lastLineId)) {
                this.map.removeLayer(lastLineId);
            }
            if (this.map.getSource(lastLineId)) {
                this.map.removeSource(lastLineId);
            }
        }

        this.updateLines();
        this.calDnT();
    }

    updateLines() {
        for (let i = 0; i < this.markers.length - 1; i++) {
            let coordinates = [this.markers[i].coordinates, this.markers[i + 1].coordinates];
            let lineId = 'line' + i;
            let color = this.colors[i % this.colors.length];
            this.drawLine(coordinates, lineId, color);
        }
        this.calDnT();
    }

    calDnT() {
        if (this.markers.length < 2) {
            this.totalElement.innerText = '';
            this.directionsInfo.innerHTML = '';
            this.previousSegmentCount = 0;
            return;
        }

        let totalDistance = 0;
        let segments = [];

        for (let i = 1; i < this.markers.length; i++) {
            const [lng1, lat1] = this.markers[i - 1].coordinates;
            const [lng2, lat2] = this.markers[i].coordinates;

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

        this.directionsInfo.innerHTML = '';

        segments.forEach((distance, index) => {
            const time = distance / averageSpeed;
            const segmentElement = document.createElement('div');
            segmentElement.className = 'segment-info';
            segmentElement.style.backgroundColor = this.colors[index % this.colors.length];

            const segmentText = document.createElement('div');
            segmentText.innerHTML = `경로 ${index + 1} : ${formatDistance(distance)} <br>소요시간 : ${formatTime(time)}`;

            segmentElement.appendChild(segmentText);

            this.directionsInfo.appendChild(segmentElement);
        });

        if (segments.length > this.previousSegmentCount) {
            const lastSegment = this.directionsInfo.lastElementChild;
            lastSegment.scrollIntoView({ behavior: 'smooth', block: 'end' });
        }

        this.previousSegmentCount = segments.length;

        this.totalElement.innerHTML = `
        총 거리 : ${formatDistance(totalDistance)}<br>
        총 소요시간 : ${formatTime(totalTime)}<br>
    `;
    }


    resetMarkers() {
        for (let markerInfo of this.markers) {
            markerInfo.marker.remove();
        }
        this.markers = [];

        for (let lineId of this.lines) {
            if (this.map.getLayer(lineId + '-outline')) {
                this.map.removeLayer(lineId + '-outline');
            }
            if (this.map.getLayer(lineId)) {
                this.map.removeLayer(lineId);
            }
            if (this.map.getSource(lineId)) {
                this.map.removeSource(lineId);
            }
        }

        this.lines = [];
        this.markerId = 0;
        this.lineId = 0;
        this.totalElement.innerText = '';
        this.directionsInfo.innerHTML = '';
    }

    captureMap() {
        let mapElement = document.getElementById('map');

        if (this.currentLocationMarker) {
            this.currentLocationMarker.remove();
        }

        html2canvas(mapElement, {
            proxy: '/html2canvas/proxy.json',
            allowTaint: false,
            useCORS: true
        }).then((canvas) => {
            let base64Image = canvas.toDataURL("image/png");

            const routeData = {
                name: document.getElementById('route-name').value,
                description: document.getElementById('route-description').value,
                markers: this.getMarkersData(),
                image: base64Image
            };

            $.ajax({
                type: 'POST',
                url: 'http://localhost:9955/api/routes',
                data: JSON.stringify(routeData),
                contentType: 'application/json',
                success: function (response) {
                    console.log('Route data and image saved successfully');
                    alert("경로 데이터와 이미지가 저장되었습니다.");
                },
                error: function (data) {
                    alert("오류 발생!");
                    console.error("Error saving route data and image:", data);
                }
            });

            if (this.currentLocationMarker) {
                this.currentLocationMarker.addTo(this.map);
            }
        });
    }

    getMarkersData() {
        return this.markers.map(markerInfo => ({
            longitude: markerInfo.coordinates[0],
            latitude: markerInfo.coordinates[1]
        }));
    }
}
