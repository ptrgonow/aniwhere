$(document).ready(function() {
    const userIdInput = $("#userId");
    const userNameInput = $("#userName");
    const emailLocalInput = $("#email-local");
    const emailDomainSelect = $("#email-domain");
    const emailDomainCustomInput = $("#email-domain-custom");
    const phoneInput = $("#phone");
    const joinBtn = $("#join-btn");

    emailDomainSelect.change(function() {
        emailDomainCustomInput.toggle(this.value === "");
        checkEmail();
    });

    const getEmail = () => {
        const localPart = emailLocalInput.val();
        const domainPart = emailDomainSelect.val() === "" ? emailDomainCustomInput.val() : emailDomainSelect.val();
        return localPart + "@" + domainPart;
    };

    const checkField = (type, inputElement, validator) => {
        const value = type === "email" ? getEmail() : inputElement.val();
        const messageElement = $(`#${type}-message`);

        if (value.trim() === "") {
            messageElement.text("");
            return;
        }

        if (!validator(value)) {
            messageElement.text(getInvalidMessage(type));
            messageElement.css('color', 'red'); // 유효성 검사 실패 시 빨간 글자
        } else {
            messageElement.text("");
            checkDuplicate(type, value, messageElement);
        }
    };

    const checkDuplicate = (type, value, messageElement) => {
        $.ajax({
            url: `/user/exists`,
            type: "GET",
            data: { type: type, value: value },
            success: function(response) {
                let typeKor;
                switch (type) {
                    case "userId":
                        typeKor = "아이디";
                        break;
                    case "email":
                        typeKor = "이메일";
                        break;
                    case "phone":
                        typeKor = "휴대폰";
                        break;
                    case "userName":
                        typeKor = "이름";
                        break;
                    default:
                        typeKor = "";
                }
                const resultMessage = response ? `이미 사용중인 ${typeKor}입니다.` : `사용 가능한 ${typeKor}입니다.`;
                messageElement.text(resultMessage);
                messageElement.css('color', response ? 'red' : 'green');
            },
            error: function() {
                console.error(`Error checking duplicate for ${type}: ${value}`);
            }
        });
    };

    const getInvalidMessage = (type) => {
        switch (type) {
            case "userId":
                return "아이디는 5자 이상, 소문자와 숫자만 사용 가능합니다.";
            case "email":
                return "올바른 이메일 형식이 아닙니다.";
            case "phone":
                return "'-'를 제외한 숫자만 입력해주세요.";
            case "userName":
                return "이름은 한글로 2글자 이상 입력해야 합니다.";
            default:
                return "";
        }
    };

    const isValidUserId = () => /^[a-z0-9]{5,}$/.test(userIdInput.val());
    const isValidEmail = (email) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email);
    const isValidPhone = (phone) => /^\d{11}$/.test(phone);
    const isValidUserName = () => /^[가-힣]{2,}$/.test(userNameInput.val());

    const checkAllValid = () => {
        return isValidUserId() && isValidEmail(getEmail()) && isValidPhone(phoneInput.val()) && isValidUserName();
    };

    function checkEmail() {
        const email = getEmail();

        if (!isValidEmail(email)) {
            emailLocalInput.next('.duplicate-message').text('올바른 이메일 형식이 아닙니다.').css('color', 'red');
        } else {
            emailLocalInput.next('.duplicate-message').text('');
            checkDuplicate("email", email, emailLocalInput.next('.duplicate-message'));
        }
    }

    userIdInput.on("keyup", function() { checkField("userId", userIdInput, isValidUserId); });
    emailLocalInput.on("keyup", checkEmail);
    emailDomainSelect.on("change", checkEmail);
    emailDomainCustomInput.on("keyup", checkEmail);
    phoneInput.on("keyup", function() { checkField("phone", phoneInput, isValidPhone); });
    userNameInput.on("keyup", function() { checkField("userName", userNameInput, isValidUserName); });

    joinBtn.on('click', function() {
        if (!checkAllValid()) {
            let errorMessage = "다시 입력해주세요.";

            if (!isValidUserId()) {
                errorMessage = "아이디는 5자 이상, 소문자와 숫자만 사용 가능합니다.";
            } else if (!isValidEmail(getEmail())) {
                errorMessage = "올바른 이메일 형식이 아닙니다.";
            } else if (!isValidPhone(phoneInput.val())) {
                errorMessage = "'-'를 제외한 숫자만 입력해주세요.";
            } else if (!isValidUserName()) {
                errorMessage = "이름은 한글로 2글자 이상 입력해야 합니다.";
            }

            alert(errorMessage);
            return;
        }

        const join = {
            userId: userIdInput.val(),
            userPwd: $('#userPwd').val(),
            userName: userNameInput.val(),
            email: getEmail(),
            address: $('#address').val(),
            detailAddress: $('#detailAddress').val(),
            zipCode: $('#zipCode').val(),
            phone: phoneInput.val()
        };

        console.log("Join data to be sent:", join); // 로그 추가

        $.ajax({
            type: 'POST',
            url: '/user/joinProc',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(join),
            success: function(response) {
                if (response === "success") {
                    alert("회원가입이 완료되었습니다.");
                    location.href = '/login';
                } else {
                    alert("회원가입에 실패했습니다. 다시 시도해주세요.");
                }
            },
            error: function(xhr, status, error) {
                console.error("회원가입 중 오류 발생:", status, error);
                alert("회원가입 중 오류가 발생했습니다.");
            }
        });
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

// 이메일 인증 코드 발송
let sentAuthCode = "";

function sendAuthCode() {
    const emailLocal = $('#email-local').val();
    const emailDomain = $('#email-domain').val();
    const email = `${emailLocal}@${emailDomain}`;

    $.ajax({
        type: 'POST',
        url: '/user/sendemail',
        data: { email: email },
        success: function(authCode) {
            alert('인증 코드가 발송되었습니다.');
            sentAuthCode = authCode; // 전송된 인증 코드를 저장
            $('#auth-code-section').show(); // 인증 코드 입력 필드 표시
        },
        error: function(error) {
            console.error('Error:', error);
            alert('인증 코드 발송에 실패했습니다.');
        }
    });
}

function verifyAuthCode() {
    const inputAuthCode = $('#auth-code').val();
    if (inputAuthCode === sentAuthCode) {
        alert('인증이 성공적으로 완료되었습니다.');
        $('#auth-code-message').text("인증 완료");
    } else {
        alert('인증 코드가 올바르지 않습니다.');
        $('#auth-code-message').text("인증 실패");
    }
}