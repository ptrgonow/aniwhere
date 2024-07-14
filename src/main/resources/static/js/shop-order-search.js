document.addEventListener('DOMContentLoaded', function () {
    // 날짜 범위 선택기 초기화
    const dateRangePicker = new AirDatepicker('#date-range', {
        locale: {
            days: ['일', '월', '화', '수', '목', '금', '토'],
            daysShort: ['일', '월', '화', '수', '목', '금', '토'],
            daysMin: ['일', '월', '화', '수', '목', '금', '토'],
            months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            monthsShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            today: '오늘',
            clear: '지우기',
            dateFormat: 'yyyy-MM-dd',
            timeFormat: 'HH:mm',
            firstDay: 0
        },
        range: true,
        multipleDatesSeparator: ' ~ ',
        onSelect: function (formattedDate) {
            console.log(formattedDate);
        }
    });

    // 검색 버튼 클릭 이벤트 핸들러 추가
    const searchButton = document.getElementById('search-button');
    if (searchButton) {
        searchButton.addEventListener('click', searchOrders);
    } else {
        console.error('Search button not found');
    }
});

function ajaxGo(url, formData, callback) {
    $.ajax({
        url: url,
        method: 'GET',
        data: formData,
        processData: false, // Add this line
        contentType: false, // Add this line
        success: function(response) {
            // Call the callback function and pass the response
            callback(response);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // Call the callback function and pass the error
            callback(new Error('Error fetching orders: ' + textStatus));
        }
    });
}

function searchOrders() {
    let dateStr = $('#date-range').val();

    console.log();

    // _airDatepicker 객체 확인
    //const dateRangeElement = document.querySelector('#date-range');
    if (dateStr == null) {
        alert('날짜 선택기가 초기화되지 않았습니다.');
        return;
    }

    //const dateRangePicker = dateRangeElement._airDatepicker;
    const dates = dateStr.split(' ~ ');

    if (!dates || dates.length !== 2) {
        alert('시작 날짜와 끝 날짜를 선택해주세요.');
        return;
    }

    const startDate = dates[0];
    const endDate = dates[1];
    console.log(startDate);
    console.log(endDate);
    console.log('데이터 보냄');

    let formData = new FormData();
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);
    ajaxGo('/api/orders/date', formData, function(result){
        result.forEach(order => {
            console.log(order);
        });
    });
}