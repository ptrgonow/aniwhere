$(document).ready(function () {
    initializeDateRangePicker();
    //bindSearchButtonClick();
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

    ajaxGet('/api/order-search?' + formData.toString(), function(result){
        if (!result.hjOrderDTOList || result.hjOrderDTOList.length === 0) {
            alert('검색 결과가 없습니다.');
        } else {
            const $acoList = $('#aco-list'); // 여기서 변수 정의
            $acoList.empty(); // 기존 내용을 지움

            let content = '';

            $.each(result.hjOrderDTOList, function(index, order) {
                content += `
                            <div class="accordion-item">
                                <h2 class="accordion-header">
                                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" aria-expanded="false">
                                        주문 ID: ${order.order_id} - 사용자 ID: ${order.user_id} - 금액: ${order.amount} - 상태: ${order.order_status}
                                    </button>
                                </h2>
                                <div class="accordion-collapse collapse">
                                    <div class="accordion-body">
                                        <span>
                                            <strong>주문 ID:</strong> ${order.order_id}<br>
                                            <strong>사용자 ID:</strong> ${order.user_id}<br>
                                            <strong>배송 주소 1:</strong> ${order.shipping_address1}<br>
                                            <strong>배송 주소 2:</strong> ${order.shipping_address2}<br>
                                            <strong>배송 주소 3:</strong> ${order.shipping_address3}<br>
                                            <strong>금액:</strong> ${order.amount}<br>
                                            <strong>주문 상태:</strong> ${order.order_status}<br>
                                            <strong>주문 날짜:</strong> ${new Date(order.order_date).toLocaleString()}<br>
                                            <strong>수령인 이름:</strong> ${order.recipient_name}<br>
                                            <strong>수령인 이메일:</strong> ${order.recipient_email}<br>
                                            <strong>수령인 전화번호:</strong> ${order.recipient_phone}<br>
                                            <strong>주문 요청:</strong> ${order.order_request}<br>
                                            <strong>결제 유형:</strong> ${order.payment_type}<br>
                                            <strong>결제 키:</strong> ${order.payment_key}
                                        </span>
                                    </div>
                                </div>
                            </div>`;
            });

            $acoList.html(content); // 모든 항목을 한 번에 추가
            initializeAccordionItems(); // 새로운 아코디언 항목 초기화
        }
    });
}

function initializeAccordionItems() {
    $('.accordion-item').each(function (index, item) {
        const $button = $(item).find('.accordion-button');
        const $collapseDiv = $(item).find('.accordion-collapse');

        // 고유한 id 생성
        const collapseId = `dynamicCollapse${index + 1}`;

        // button과 collapseDiv에 id 및 data-bs-target 설정
        $button.attr('data-bs-target', `#${collapseId}`);
        $button.attr('aria-controls', collapseId);
        $collapseDiv.attr('id', collapseId);
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