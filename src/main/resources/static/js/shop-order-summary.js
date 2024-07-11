document.addEventListener('DOMContentLoaded', function () {
    // URL에서 orderId를 가져옵니다.
    const params = new URLSearchParams(window.location.search);
    const orderId = params.get('orderId');

    if (!orderId) {
        alert('주문 ID가 제공되지 않았습니다.');
        return;
    }

    // 서버에서 주문 데이터를 가져옵니다.
    fetch(`/api/orders/${orderId}`)
        .then(response => response.json())
        .then(order => {
            if (!order) {
                alert('주문 데이터를 가져오지 못했습니다.');
                return;
            }

            // 주문 데이터를 HTML에 채웁니다.
            document.getElementById('order-date-time').innerText = order.orderDateTime;
            document.getElementById('order-id').innerText = order.orderId;
            document.getElementById('invoice-id').innerText = order.invoiceId; // 추가된 부분
            document.getElementById('order-status').innerText = order.orderStatus;
            document.getElementById('total-amount').innerText = order.totalAmount;
            document.getElementById('customer-name').innerText = order.customerName;
            document.getElementById('customer-id').innerText = order.customerId;
            document.getElementById('customer-email').innerText = order.customerEmail;
            document.getElementById('customer-phone').innerText = order.customerPhone;
            document.getElementById('recipient-name').innerText = order.recipientName;
            document.getElementById('shipping-address').innerText = `${order.shippingAddress1} ${order.shippingAddress2} ${order.shippingAddress3}`;
            document.getElementById('shipping-company').innerText = order.shippingCompany;
            document.getElementById('estimated-arrival-date').innerText = order.estimatedArrivalDate;
            document.getElementById('shipping-request').innerText = order.shippingRequest;
            document.getElementById('product-category').innerText = order.productCategory;
            document.getElementById('product-name').innerText = order.productName;
            document.getElementById('product-color').innerText = order.productColor;
            document.getElementById('product-size').innerText = order.productSize;
            document.getElementById('product-quantity').innerText = order.productQuantity;
            document.getElementById('product-unit-price').innerText = order.productUnitPrice;
            document.getElementById('product-discounted-price').innerText = order.productDiscountedPrice;
            document.getElementById('payment-method').innerText = order.paymentMethod;
            document.getElementById('payment-date-time').innerText = order.paymentDateTime;
            document.getElementById('payment-amount').innerText = order.paymentAmount;
            document.getElementById('bank-name').innerText = order.bankName;
            document.getElementById('account-holder').innerText = order.accountHolder;
            document.getElementById('account-number').innerText = order.accountNumber;
            document.getElementById('deposited-account').innerText = order.depositedAccount;
        })
        .catch(error => {
            console.error('Error fetching order data:', error);
            alert('주문 데이터를 가져오는 데 오류가 발생했습니다.');
        });
});
