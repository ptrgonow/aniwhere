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

            let content = '';

            $.each(groupedOrders, function(orderId, orders) {
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

                // "더 보기" 버튼 추가 조건
                if(orders.length > 1){
                    content += `
                        <button type="button" class="btn btn-link more-info-button" data-order-id="${order.order_id}">더 보기</button>`;
                }

                // 각 주문에 연결된 product_id를 표시
                $.each(orders, function(index, product) {
                    content += `
                        <div class="accordion-body-more-info">
                            <div class="paragraph1">
                                <div class="product-name">제품명: ${product.product_name}</div>
                            </div>
                            <div class="paragraph2">
                                <div class="product-id"><strong>제품 아이디:</strong>${product.product_id}</div>
                                <div class="order-status"><strong>상태:</strong> ${product.order_status}</div>
                                <div class="order-quantity"><strong>수량:</strong> ${product.quantity}</div>
                                <div class="order-price"><strong>가격:</strong> ${product.price}</div>
                            </div>
                            <div class="paragraph3">
                                <img src="${product.product_image}" alt="상품 이미지">
                            </div>
                        </div>`;
                });


                content += `</div>
                        </div>
                    </div>`;
            });

            $acoList.html(content);
            initializeAccordionItems();
            bindMoreInfoButtons(); // "더 보기" 버튼에 이벤트 바인딩
        }
    });
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
    $(document).on('click', '.more-info-button', function () {
        const $button = $(this);
        const order_id = $button.data('.order-id');

        const $accordionBody = $button.closest('.accordion-body');
        const $moreInfo = $accordionBody.find('.more-info');

        if ($moreInfo.is(':empty')) {
            ajaxGet('/api/order-details?order_id=' + order_id, function(result) {
                if (result && result.length > 0) {
                    let moreInfoContent = '';
                    $.each(result, function(index, detail) {
                        moreInfoContent += `
                            <div class="accordion-body-more-info">
                            
                                <div class="paragraph2">
                                    <div class="product-id"><strong>제품 아이디:</strong>${product.product_id}</div>
                                    <div class="order-status"><strong>상태:</strong> ${product.order_status}</div>
                                    <div class="order-quantity"><strong>수량:</strong> ${product.quantity}</div>
                                    <div class="order-price"><strong>가격:</strong> ${product.price}</div>
                                </div>
                                <div class="paragraph3">
                                    <img src="${product.product_image}" alt="상품 이미지">
                                </div>
                            </div>`;
                    });
                    $moreInfo.html(moreInfoContent);
                } else {
                    $moreInfo.html('<div>추가 정보가 없습니다.</div>');
                }
            });
        }

        $moreInfo.toggle();
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
