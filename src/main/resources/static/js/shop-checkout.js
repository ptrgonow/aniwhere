$(document).ready(function(){
    toss().then(r => console.log("done"));

    // 스크롤 이벤트
    const $sticky = $('.sticky');
    const stickyTop = $sticky.offset().top;

    $(window).scroll(function() {
        const windowTop = $(window).scrollTop();

        if (windowTop > stickyTop) {
            $sticky.addClass('scrolled');
        } else {
            $sticky.removeClass('scrolled');
        }
    });

    $(".del-check").on("change", function() {
        const isChecked = $(this).is(":checked");
        const $userNameInput = $("#shipping-form input[type='text'][placeholder='수취인']");
        const $userEmailInput = $("#shipping-form input[type='email'][placeholder='이메일']");
        const $userPhoneInput = $("#shipping-form input[type='tel'][placeholder='연락처']");
        const $userAddressInput = $("#shipping-form input[name='address']");
        const $userDetailAddressInput = $("#shipping-form input[name='detailAddress']");
        const $userZipCodeInput = $("#shipping-form input[name='zipCode']");


        if (isChecked) {

            $userNameInput.val($("input[type='text'][readonly]").val());
            $userEmailInput.val($("input[type='email'][readonly]").val());
            $userPhoneInput.val($("input[type='tel'][readonly]").val());
            $userAddressInput.val($("input[name='address'][readonly]").val());
            $userDetailAddressInput.val($("input[name='detailAddress'][readonly]").val());
            $userZipCodeInput.val($("input[name='zipCode'][readonly]").val());
        } else {

            $userNameInput.val('');
            $userEmailInput.val('');
            $userPhoneInput.val('');
            $userAddressInput.val('');
            $userDetailAddressInput.val('');
            $userZipCodeInput.val('');
        }
    });

    // 새로운 배송지 클릭 시 입력값 초기화
    $(".new-del").on("click", function(event){
        event.preventDefault();
        event.stopPropagation(); // 아코디언 이벤트 막기
        $("#shipping-form").find("input").val("");
    });

});

// 주소 찾기
function searchAdd() {
    new daum.Postcode({
        oncomplete: function(data) {
            let addr = ''; // 주소
            let extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("detailAddress").value = extraAddr;

            } else {
                document.getElementById("detailAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('address').value = data.zonecode;
            document.getElementById("detailAddress").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("zipCode").focus();
        }
    }).open();
}


// 토스페이먼츠 결제위젯
async function toss() {
    const button = document.getElementById("payment-button");
    const totalPriceElement = document.getElementById("totalPrice"); // #totalPrice 요소 선택 (total-price가 아닌 totalPrice)
    const totalPriceText = totalPriceElement.innerText;
    const totalPrice = parseInt(totalPriceText.replace(/[^0-9]/g, ''), 10); // 숫자만 추출


    //결제위젯 초기화
    const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";
    const tossPayments = TossPayments(clientKey);
    // 회원 결제
    const customerKey = "mS9YrqdNqrR54mVQ_DbmA";
    const widgets = tossPayments.widgets({
        customerKey,
    });

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
        const customerEmail = document.querySelector('input[name="email"]').value;
        const customerName = document.querySelector('input[name="userName"]').value;
        const customerMobilePhone = document.querySelector('input[name="phone"]').value.replace(/-/g, '');
        const recipientAddress = document.querySelector('#address').value;
        const recipientDetailAddress = document.querySelector('#detailAddress').value;
        const recipientZipCode = document.querySelector('#zipCode').value;
        const orderRequest = document.querySelector('#require-content').value;
        const orderId = document.querySelector('#orderId').value;

        const orderDetails = {
            orderId: orderId,
            amount: totalPrice,
            recipientEmail: customerEmail,
            recipientName: customerName,
            recipientPhone: customerMobilePhone,
            shippingAddress1 : recipientAddress,
            shippingAddress2 : recipientDetailAddress,
            shippingAddress3 : recipientZipCode,
            orderRequest: orderRequest
        }

        // oderDetails에 한 필드라도 값이 존재하지 않는다면 alert
        if (Object.values(orderDetails).some(value => !value)) {
            alert("주문 정보를 입력해주세요.");
            return;
        }

        try {
            console.log("상품정보", orderDetails)

            $.ajax({
                type: "POST",
                url: "/orders/success",
                contentType: "application/json",
                data: JSON.stringify(orderDetails),
                success: function(response) {
                    console.log('주문 정보 저장 성공:', response);
                    widgets.requestPayment({
                        orderId: orderId,
                        orderName: "AniMall " + orderId,
                        successUrl: `${window.location.origin}/shop/success?customerEmail=${encodeURIComponent(customerEmail)}&customerMobilePhone=${encodeURIComponent(customerMobilePhone)}&customerName=${encodeURIComponent(customerName)}`,
                        failUrl: window.location.origin + "/fail.html",
                        customerEmail: customerEmail,
                        customerName: customerName,
                        customerMobilePhone: customerMobilePhone,
                    });
                },
                error: function(error) {
                    console.error('주문 정보 저장 실패:', error);
                    alert("주문 정보 저장 중 오류가 발생했습니다. 다시 시도해주세요." + error);

                }
            });

        } catch (error) {
            // 주문 정보 저장 실패, 결제 실패 또는 서버 응답 오류 처리
            console.error("결제 처리 중 오류 발생:", error);
            alert("결제 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    });


}
