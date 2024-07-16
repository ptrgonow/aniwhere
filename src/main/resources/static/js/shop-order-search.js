$(document).ready(function () {
    initializeDateRangePicker();
    bindSearchButtonClick();
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

function bindSearchButtonClick() {
    $('#search-button').on('click', searchOrders);
}

function searchOrders() {
    let dateStr = $('#date-range').val();
    if (!dateStr) {
        alert('날짜 선택기가 초기화되지 않았습니다.');
        return;
    }

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

    let formData = new URLSearchParams();
    formData.append('startDate', startDate);
    formData.append('endDate', endDate);

    ajaxGet('/api/order-images?' + formData.toString(), function(result) {
        if (!result.hjOrderDTOList || result.hjOrderDTOList.length === 0) {
            alert('검색 결과가 없습니다.');
        } else {
            const $acoList = $('#aco-list');
            $acoList.empty();

            // 주문 목록을 order_id를 기준으로 그룹화
            let groupedOrders = groupOrdersByOrderId(result.hjOrderDTOList);

            // 주문 목록을 날짜 기준으로 정렬 (최근 날짜가 먼저)
            groupedOrders = Object.entries(groupedOrders).sort((a, b) => {
                const dateA = new Date(a[1][0].order_date);
                const dateB = new Date(b[1][0].order_date);
                return dateB - dateA; // 최근 날짜가 먼저 오도록 정렬
            });

            let content = '';

            $.each(groupedOrders, function(_, [orderId, orders]) {
                const order = orders[0]; // 같은 주문 ID는 동일한 정보를 가지므로 첫 번째 항목을 사용
                const orderDate = new Date(order.order_date).toLocaleDateString('ko-KR', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                });

                content += `
                    <div class="accordion-item">
                        <h2 class="accordion-header">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" aria-expanded="false">
                                <div class="order-id"><strong>주문 ID:</strong> ${order.order_id}</div>
                                <div class="order-date"><strong>주문 날짜:</strong> ${orderDate}</div>
                            </button>
                        </h2>
                        <div class="accordion-collapse collapse">
                            <div class="accordion-body">`;

                // 각 주문에 대해 첫 번째 제품 정보만 표시
                content += getProductContent(orders[0]);

                // "더 보기" 버튼 추가 조건
                if (orders.length > 1) {
                    content += `
                        <div class="more-info">`;

                    // 나머지 제품 정보는 숨김 상태로 추가
                    for (let i = 1; i < orders.length; i++) {
                        content += getProductContent(orders[i]);
                    }

                    content += `
                        </div>
                        <button type="button" class="btn btn-link more-info-button" data-order-id="${order.order_id}">더 보기</button>`;
                }

                content += `
                            </div>
                        </div>
                    </div>`;
            });

            $acoList.html(content);
            initializeAccordionItems();
            bindMoreInfoButtons(); // "더 보기" 버튼에 이벤트 바인딩
        }
    });
}

function getProductContent(product) {
    return `
        <div class="accordion-body-more-info">
            <div class="paragraph1">
                <div class="product-name">${product.product_name}</div>
            </div>
            <div class="paragraph2">
                <div class="product-id"><strong>제품 아이디:</strong> ${product.product_id}</div>
                <div class="order-status"><strong>상태:</strong> ${product.order_status}</div>
                <div class="order-quantity"><strong>수량:</strong> ${product.quantity}</div>
                <div class="order-price"><strong>가격:</strong> ${product.price}</div>
            </div>
            <div class="paragraph3">
                <img src="${product.product_image}" alt="상품 이미지">
            </div>
        </div>`;
}

function groupOrdersByOrderId(orders) {
    return orders.reduce((acc, order) => {
        if (!acc[order.order_id]) {
            acc[order.order_id] = [];
        }
        acc[order.order_id].push(order);
        return acc;
    }, {});
}

function initializeAccordionItems() {
    $('.accordion-item').each(function (index, item) {
        const $button = $(item).find('.accordion-button');
        const $collapseDiv = $(item).find('.accordion-collapse');

        const collapseId = `dynamicCollapse${index + 1}`;
        $button.attr('data-bs-target', `#${collapseId}`);
        $button.attr('aria-controls', collapseId);
        $collapseDiv.attr('id', collapseId);
    });
}

function bindMoreInfoButtons() {
    $(document).on('click', '.more-info-button', function (e) {
        e.stopPropagation(); // 이벤트 전파 중단
        const $button = $(this);
        const $moreInfo = $button.siblings('.more-info');

        if ($button.text() === '더 보기') {
            $moreInfo.css('height', 'auto'); // 콘텐츠 높이를 자동으로 설정
            let autoHeight = $moreInfo.height(); // auto 높이를 계산
            $moreInfo.height(0); // 높이를 0으로 설정
            $moreInfo.stop().animate({ height: autoHeight }, 300); // 높이를 애니메이션으로 조정
            $button.text('접기');
        } else {
            $moreInfo.stop().animate({ height: 0 }, 300, function() {
                $moreInfo.css('height', 'auto'); // 애니메이션 후 높이를 auto로 되돌림
            });
            $button.text('더 보기');
        }
    });

    // 아코디언 헤더를 클릭할 때, 더 보기 상태인지 확인
    $(document).on('click', '.accordion-button', function () {
        const $accordionBody = $(this).closest('.accordion-item').find('.accordion-body');
        const $moreInfo = $accordionBody.find('.more-info');

        // 아코디언을 열 때, 더 보기 상태인지 확인
        if ($moreInfo.is(':visible')) {
            $moreInfo.css('height', '0');
            $(this).closest('.accordion-item').find('.more-info-button').text('더 보기');
        }
    });
}

function ajaxGet(url, callback) {
    $.ajax({
        url: url,
        method: 'GET',
        success: function(response) {
            callback(response);
        },
        error: function(jqXHR, textStatus) {
            callback(new Error('주문 검색 중 오류 발생: ' + textStatus));
        }
    });
}
