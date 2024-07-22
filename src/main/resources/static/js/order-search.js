$(document).ready(function () {

    const userId = $('#userId').val();

    initializeDateRangePicker();

    $('#search-button').on('click', function (e) {
        searchOrders(userId, e);
    });


});

function initializeDateRangePicker() {
    new AirDatepicker('#date-range', {
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
}

function searchOrders(userId, e) {
    e.preventDefault();

    let dateStr = $('#date-range').val();
    if (!dateStr) {
        alert('날짜를 선택해주세요.');
        return;
    }

    const dates = dateStr.split(' ~ ');

    let startDate, endDate;

    if (dates.length === 1) {
        startDate = dates[0];
        endDate = dates[0];
    } else if (dates.length === 2) {
        startDate = dates[0];
        endDate = dates[1];
    } else {
        alert('유효한 날짜를 선택해주세요.');
        return;
    }

    console.log('Start Date:', startDate);
    console.log('End Date:', endDate);

    let formData = new URLSearchParams();
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);
    formData.append('userId', userId);

    $.ajax({
        url: '/orders/search?' + formData.toString(),
        method: 'GET',
        success: function(searchItems) {
            console.log(searchItems); // 응답 데이터 확인
            if (!searchItems || searchItems.length === 0) {
                alert('검색 결과가 없습니다.');
                return;
            }

            const acoList = $('#aco-list');
            acoList.empty();

            let content = '';
            let ordersGroupedByOrderId = {};

            // 주문 아이디로 그룹화
            searchItems.forEach(item => {
                if (!ordersGroupedByOrderId[item.orderId]) {
                    ordersGroupedByOrderId[item.orderId] = [];
                }
                ordersGroupedByOrderId[item.orderId].push(item);
            });

            Object.keys(ordersGroupedByOrderId).forEach((orderId, index) => {
                const orderItems = ordersGroupedByOrderId[orderId];
                const firstItem = orderItems[0];

                content += `
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${index}" aria-expanded="false">
                                <div class="order-id"><strong>주문 ID:</strong> ${firstItem.orderId}</div>
                                <div class="order-date"><strong>주문 날짜:</strong> ${firstItem.orderDate}</div>
                            </button>
                        </h2>
                        <div id="collapse${index}" class="accordion-collapse collapse">
                            <div class="accordion-body">
                                ${getProductContent(firstItem)}`; // 첫 번째 상품 표시

                if (orderItems.length > 1) {
                    content += `
                                <div class="accordion-body more-info">`;

                    for (let i = 1; i < orderItems.length; i++) {
                        content += getProductContent(orderItems[i]);
                    }
                    content += `
                                </div>
                            </div>
                            <div class="btn-div">
                                <button type="button" class="more-info-button btn-info" data-order-id="${firstItem.orderId}">더 보기</button>
                            </div>
                            `;
                    }

                content += `
                        </div>
                    </div>`;
            });

            acoList.html(content);

            // 아코디언 항목 및 "더 보기" 버튼 기능 초기화
            initializeAccordionItems();
            initializeMoreInfoButtons();
        },
        error: function() {
            alert('검색 중 오류가 발생했습니다.');
        }
    });
}

function getProductContent(product) {
    const productName = product.productName || '상품 이름 없음';
    const productId = product.productId || 'N/A';
    const orderStatus = product.orderStatus || '상태 없음';
    const totalQuantity = product.totalQuantity || 0;
    const totalAmount = product.totalAmount || 0;
    const image = product.image || '이미지 없음';

    return `
        <div class="accordion-body-more-info">
            <div class="paragraph1">
                <div class="product-name">${productName}</div>
            </div>
            <div class="paragraph2">
                <div class="product-id"><strong>제품 아이디:</strong> ${productId}</div>
                <div class="order-status"><strong>상태:</strong> ${orderStatus}</div>
                <div class="order-quantity"><strong>수량:</strong> ${totalQuantity}</div>
                <div class="order-price"><strong>가격:</strong> ${totalAmount}</div>
            </div>
            <div class="paragraph3">
                <img src="${image}" alt="상품 이미지">
            </div>
        </div>`;
}

function initializeMoreInfoButtons() {
    $(document).on('click', '.more-info-button', function() {
        const button = $(this);
        const moreInfo = button.closest('.accordion-item').find('.more-info');
        moreInfo.toggleClass('show');

        if (moreInfo.hasClass('show')) {
            button.text('접기');
        } else {
            button.text('더 보기');
        }
    });
}

function initializeAccordionItems() {
    $(document).on('click', '.accordion-button', function() {
        const button = $(this);
        const target = button.attr('data-bs-target');
        $(target).collapse('toggle');
    });
}
