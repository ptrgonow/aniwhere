$(document).ready(function(){
    toss().then(r => console.log("done"));
});

async function toss() {
    const button = document.getElementById("payment-button");
    const totalPriceElement = document.getElementById("total-price");
    let totalPrice = parseInt(totalPriceElement.innerText);

    //결제위젯 초기화
    const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
    const tossPayments = TossPayments(clientKey);
    // 회원 결제
    const customerKey = "mS9YrqdNqrR54mVQ_DbmA";
    const widgets = tossPayments.widgets({
        customerKey,
    });
    // 비회원 결제
    // const widgets = tossPayments.widgets({ customerKey: TossPayments.ANONYMOUS });

    //주문의 결제 금액 설정
    await widgets.setAmount({
        currency: "KRW",
        value: totalPrice,
    });

    //결제 UI 렌더링
    await widgets.renderPaymentMethods({
        selector: "#payment-method",
        variantKey: "DEFAULT",
    });

    //이용약관 UI 렌더링
    await widgets.renderAgreement({ selector: "#agreement", variantKey: "AGREEMENT" });


    //'결제하기' 버튼 누르면 결제창 띄우기
    button.addEventListener("click", async function () {
        await widgets.requestPayment({
            orderId: "DMzckbI76qfdcWQPzeokD",
            orderName: "토스 티셔츠 외 2건",
            successUrl: window.location.origin + "/success.html",
            failUrl: window.location.origin + "/fail.html",
            customerEmail: "customer123@gmail.com",
            customerName: "김토스",
            customerMobilePhone: "01012341234",
        });
    });
}