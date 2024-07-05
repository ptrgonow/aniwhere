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

    // 이메일 주소 조합 함수
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

        if (!validator(value)) { // 유효성 검사 실패
            messageElement.text(getInvalidMessage(type));
        } else {
            messageElement.text("");
            checkDuplicate(type, value);
        }
    };

    // 중복 검사 함수
    const checkDuplicate = (type, value) => {
        $.ajax({
            url: `/user/exists?type=${type}&value=${value}`,
            type: "GET",
            success: function(response) {
                const typeKor = type === "userId" ? "아이디" : (type === "email" ? "이메일" : "휴대폰");
                const resultMessage = response ? `이미 사용중인 ${typeKor}입니다.` : (type === "userId" ? "사용 가능한 아이디입니다." : "");
                $(`#${type}-message`).text(resultMessage);
            }
        });
    };

    // 유효성 검사 실패 시 메시지 반환 함수
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

    // 유효성 검사 함수들 (모듈화)
    const isValidUserId = () => /^[a-z0-9]{5,}$/.test(userIdInput.val());
    const isValidEmail = (email) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email);
    const isValidPhone = (phone) => /^\d{11}$/.test(phone);
    const isValidUserName = () => /^[가-힣]{2,}$/.test(userNameInput.val());

    // 모든 입력 필드 유효성 검사 함수 (버튼 비활성화 로직 제거)
    const checkAllValid = () => {
        return isValidUserId() && isValidEmail(getEmail()) && isValidPhone(phoneInput.val()) && isValidUserName();
    };

    // 이메일 주소 조합 및 중복 검사 함수
    function checkEmail() {
        const email = getEmail();

        if (!isValidEmail(email)) {
            emailLocalInput.next('.duplicate-message').text('올바른 이메일 형식이 아닙니다.');
        } else {
            emailLocalInput.next('.duplicate-message').text('');
            checkDuplicate("email", email);
        }
    }

    // 각 입력 필드에 대한 이벤트 핸들러 등록
    userIdInput.on("keyup", function() { checkField("userId", userIdInput, isValidUserId); });
    emailLocalInput.on("keyup", checkEmail);
    emailDomainSelect.on("change", checkEmail);
    emailDomainCustomInput.on("keyup", checkEmail);
    phoneInput.on("keyup", function() { checkField("phone", phoneInput, isValidPhone); });
    userNameInput.on("keyup", function() { checkField("userName", userNameInput, isValidUserName); });

    // 회원가입 버튼 클릭 이벤트 처리
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
            error: function() {
                alert("회원가입 중 오류가 발생했습니다.");
            }
        });
    });
});
