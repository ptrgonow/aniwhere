$(document).ready(function() {

    let userId = $('#hiddenUserId').val();

    // 비밀번호 확인
    function checkPassword() {
        const userPwd = $('#userPwd').val();

        if (userId === null) {
            alert("사용자 ID를 가져오는 데 실패했습니다.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/mypage/pwdcheck",
            contentType: "application/json",
            data: JSON.stringify({
                userId: userId,
                userPwd: userPwd
            }),
            success: function(response) {
                alert(response);
                $('#checkButton').html('<span class="material-symbols-outlined" id="check-icon">task_alt</span>');
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
                console.error('Status:', status);
                console.error('XHR:', xhr);
                alert("API 통신 실패: " + xhr.responseText);
            }
        });
    }

    // 확인 버튼 클릭 이벤트 핸들러
    $('#checkButton').click(function() {
        checkPassword();
    });

    // 수정 버튼 클릭 시
    $("#modi-btn").on("click", function() {
        const userId = $("#userId").val();
        const newPwd = $("#userNewPwd").val();
        const userName = $("#userName").val();
        const address = $("#address").val();
        const detailAddress = $("#detailAddress").val();
        const zipCode = $("#zipCode").val();
        const phone = $("#phone").val();

        $.ajax({
            type: "PUT",
            url: "/mypage/update",
            contentType: "application/json",
            data: JSON.stringify({
                userId: userId,
                userPwd: newPwd,
                userName: userName,
                address: address,
                detailAddress: detailAddress,
                zipCode: zipCode,
                phone: phone
            }),
            success: function(response) {
                alert("정보 수정이 완료되었습니다.");
                location.reload();
            },
            error: function(xhr, error) {
                console.error('Error:', error);
                alert(xhr.responseText);
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