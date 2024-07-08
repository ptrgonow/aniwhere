$(document).ready(function() {

toss()
    .then(r => console.log("done"));

});

async function toss() {
    const paymentBtn = $('#payment-button');
    const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
    const tossPayments = TossPayments(clientKey);
    const customerKey = "0_z24fSU34br_wMjrutPL";
    const amount = parseInt($('#amount').text(), 10);
    const widgets = tossPayments.widgets({
        customerKey,
    });


    await widgets.setAmount({
        currency: "KRW",
        value: amount,
    });

    await widgets.renderPaymentMethods({
        selector: "#payment-method",
        variantKey: "DEFAULT",
    });

    await widgets.renderAgreement({
        selector: "#agreement",
        variantKey: "AGREEMENT",
    });

    paymentBtn.on("click", async function () {

        await widgets.requestPayment({
            orderId: "PFcVYymZpUXqyezP6211D",
            orderName: "토스 티셔츠 외 2건",
            successUrl: window.location.origin + "/success.html",
            failUrl: window.location.origin + "/fail.html",
            customerEmail: "customer123@gmail.com",
            customerName: "김토스",
            customerMobilePhone: "01012341234",
        });

    });

}
