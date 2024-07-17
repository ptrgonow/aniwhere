$(document).ready(function() {
    const orderDetailTable = $("#orderDetail");
    let currentOrderData = null;

    // 초기 주문 상세 정보 표시 함수
    function showfirstorderdetail() {
        const firstOrderId = $('.table.text-nowrap.mb-0.align-middle tbody tr:first-child').data('orderid');
        if (firstOrderId) {
            fetchOrderDetails(firstOrderId);
        }
    }

    function fetchOrderDetails(orderId) {
        $.ajax({
            url: '/admin/dash/list/' + orderId,
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                displayOrderDetails(data.order, data.orderDetails);
                currentOrderData = data.order;
            },
            error: function(xhr, status, error) {
                console.error('주문 세부 정보 가져오기 오류:', error);
                displayErrorMessage('주문 세부 정보를 가져오는 중 오류가 발생했습니다.');
            }
        });
    }
    function displayOrderDetails(order, orderDetails) {
        orderDetailTable.empty();

        const orderInfoRows = `
        <tbody>
        <tr>
            <th class="order-detail-th">주문번호</th>
            <td>${order.orderId}</td>
        </tr>
         <tr>
            <th class="order-detail-th">아이디(이름)</th>
            <td>${order.isSocial === 1 ? order.userName + ' (소셜 로그인 사용자)' : order.userId}</td> 
        </tr>
        <tr>
            <th class="order-detail-th">결제금액</th>
            <td>${order.amount.toLocaleString()}원</td>
        </tr>
        <tr>
            <th class="order-detail-th">주소</th>
            <td>${order.shippingAddress1} ${order.shippingAddress2} ${order.shippingAddress3}</td>
        </tr>
        <tr>
            <th class="order-detail-th">수령인</th>
            <td>${order.recipientName}</td>
        </tr>
        <tr>
            <th class="order-detail-th">연락처</th>
            <td>${order.recipientPhone}</td>
        </tr>
        <tr>
            <th class="order-detail-th">요청사항</th>
            <td>${order.orderRequest || ''}</td>
        </tr>
        </tbody>
    `;
        orderDetailTable.append(orderInfoRows);

        const productItems = orderDetails.map(item => `
        <div class="product-item">
            <span class="product-name">${item.name}</span>
            <span class="product-quantity">수량: ${item.quantity}</span>
            <span class="product-price">가격: ${item.price.toLocaleString()}원</span>
        </div>
    `).join('');

        const productRow = `
        <tbody>
        <tr>
            <th class="order-detail-th">주문 상품</th>
            <td>${productItems}</td>
        </tr>
        </tbody>
    `;
        orderDetailTable.append(productRow);
        const statusSelect = $(`
            <select class="form-select order-status-select" data-order-id="${order.orderId}">
                <option value="결제 완료" ${order.orderStatus === '결제 완료' ? 'selected' : ''}>결제 완료</option>
                <option value="배송 준비" ${order.orderStatus === '배송 준비' ? 'selected' : ''}>배송 준비</option>
                <option value="배송 출발" ${order.orderStatus === '배송 출발' ? 'selected' : ''}>배송 출발</option>
            </select>
        `);

        const updateStatusButton = $('<button class="btn btn-primary update-status-btn">상태 변경</button>');

        const statusRow = $(`
            <tr>
                <th class="order-detail-th">주문 상태</th>
                <td id="orderStatus"></td> 
            </tr>
        `);
        statusRow.find('td').append(statusSelect, updateStatusButton);
        orderDetailTable.append(statusRow);

        $(document).on('click', '.update-status-btn', function() {
            const orderId = $(this).siblings('.order-status-select').data('orderId');
            const newStatus = $(this).siblings('.order-status-select').val();
            console.log(orderId)
            $.ajax({
                url: '/admin/dash/status/' + orderId,
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ newStatus: newStatus }),
                success: function(response) {
                    alert(response.message);
                    window.location.reload();
                },
                error: function(xhr, status, error) {
                    console.error('상태 변경 실패:', error);
                    alert('상태 변경 중 오류가 발생했습니다.');
                }
            });
        });
    }




    function displayErrorMessage(message) {
        orderDetailTable.empty();
        orderDetailTable.append(`<tr><td colspan="2">${message}</td></tr>`);
    }

    $(document).on('click', '.table.text-nowrap.mb-0.align-middle tbody tr', function() {
        const orderId = $(this).data('orderid');
        fetchOrderDetails(orderId);
    });

    // 초기 실행
    showfirstorderdetail();
});