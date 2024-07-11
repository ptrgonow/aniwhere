document.addEventListener('DOMContentLoaded', function () {
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
        multipleDatesSeparator: ' - ',
        onSelect: function (formattedDate) {
            console.log(formattedDate);
        }
    });

    document.querySelector('button[onclick="searchOrders()"]').addEventListener('click', function () {
        searchOrders(dateRangePicker);
    });
});

function searchOrders(dateRangePicker) {
    const dates = dateRangePicker.selectedDates;
    if (dates.length !== 2) {
        alert('시작 날짜와 끝 날짜를 선택해주세요.');
        return;
    }

    const startDate = dates[0].toISOString().split('T')[0];
    const endDate = dates[1].toISOString().split('T')[0];

    fetch(`/api/orders?startDate=${startDate}&endDate=${endDate}`)
        .then(response => response.json())
        .then(orders => {
            if (!orders || orders.length === 0) {
                alert('주문 데이터를 가져오지 못했습니다.');
                return;
            }

            const orderList = document.getElementById('order-results');
            orderList.innerHTML = '';

            orders.forEach(order => {
                if (order.orderId) {
                    const listItem = document.createElement('li');
                    listItem.textContent = `Order ID: ${order.orderId}, Total Amount: ${order.totalAmount}`;
                    orderList.appendChild(listItem);
                } else {
                    console.error('Order is missing orderId:', order);
                }
            });
        })
        .catch(error => {
            console.error('Error fetching orders:', error);
        });
}
