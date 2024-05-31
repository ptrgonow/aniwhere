
    function showPaymentSection() {
    const sections = document.querySelectorAll('.payment-section');
    sections.forEach(section => section.style.display = 'none');

    const selectedPayment = document.querySelector('input[name="payment"]:checked').value;
    document.getElementById(selectedPayment + '-section').style.display = 'block';
}

    document.addEventListener('DOMContentLoaded', () => {
    const paymentMethods = document.querySelectorAll('input[name="payment"]');
    paymentMethods.forEach(method => {
    method.addEventListener('change', showPaymentSection);
});

    // Show the default selected section
    showPaymentSection();
});


/*

$(document).ready(function() {
    // Handle Payment Method Selection
    $('input[name="addr_paymethod"]').on('change', function() {
        var selectedMethod = $(this).val();
        $('.ec-order-payment-card, #payment_input_cash, #payment_input_tcash, #payment_input_icash').hide();

        if (selectedMethod === 'cash') {
            $('#payment_input_cash').show();
            $('#current_pay_name').text('무통장 입금');
        } else if (selectedMethod === 'card') {
            $('#ec-order-payment-directpay-card-form').show();
            $('#ec-order-payment-directpay-card-agree').show();
            $('#current_pay_name').text('카드 결제');
        } else if (selectedMethod === 'icash') {
            $('#payment_input_icash').show();
            $('#current_pay_name').text('가상계좌');
        } else if (selectedMethod === 'tcash') {
            $('#payment_input_tcash').show();
            $('#current_pay_name').text('실시간 계좌이체');
        } else if (selectedMethod === 'cell') {
            $('#current_pay_name').text('휴대폰 결제');
        }
    });

    // Toggle Simple Join Guide
    $('#simple_join').on('change', function() {
        if ($(this).is(':checked')) {
            $('#simpleJoinGuide').show();
        } else {
            $('#simpleJoinGuide').hide();
        }
    });

});
*/