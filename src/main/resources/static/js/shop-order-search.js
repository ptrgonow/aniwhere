document.addEventListener('DOMContentLoaded', function () {
    const dateRangePicker = new AirDatepicker('#date-range', {
        locale: {
            days: ['일', '월', '화', '수', '목', '금', '토'],
            daysShort: ['일', '월', '화', '수', '목', '금', '토'],
            daysMin: ['일', '월', '화', '수', '목', '금', '토'],
            months: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            monthsShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
            today: '오늘',
            clear: '삭제',
            dateFormat: 'yyyy-MM-dd',
            timeFormat: 'hh:mm aa',
            firstDay: 0
        },
        range: true,
        multipleDatesSeparator: ' ~ ',
        onSelect({formattedDate, date, datepicker}) {
            console.log(formattedDate); // 선택된 날짜 범위 확인
        }
    });

    document.getElementById('date-range-form').addEventListener('submit', function (event) {
        event.preventDefault();
        searchOrders();
    });
});

function searchOrders() {
    const dateRange = document.getElementById('date-range').value;
    if (!dateRange) {
        alert('날짜 범위를 선택하세요.');
        return;
    }

    const [startDate, endDate] = dateRange.split(' ~ ');

    // 임의의 주문 데이터를 생성하여 검색 결과를 출력
    const orderResults = document.getElementById('order-results');
    orderResults.innerHTML = `
        <li><a href="/shop-order-summary.html?orderId=123456">주문 ID: 123456, 주문 날짜: ${startDate}</a></li>
        <li><a href="/shop-order-summary.html?orderId=789012">주문 ID: 789012, 주문 날짜: ${startDate}</a></li>
        <li><a href="/shop-order-summary.html?orderId=345678">주문 ID: 345678, 주문 날짜: ${startDate}</a></li>
        <li><a href="/shop-order-summary.html?orderId=901234">주문 ID: 901234, 주문 날짜: ${endDate}</a></li>
        <li><a href="/shop-order-summary.html?orderId=567890">주문 ID: 567890, 주문 날짜: ${endDate}</a></li>
    `;
}
