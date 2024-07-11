$(document).ready(function() {
    let userId = null;

    // 현재 로그인된 사용자 ID 가져오기
    $.ajax({
        type: "GET",
        url: "/mypage/authenticatedUserId",
        success: function(response) {
            userId = response;
            console.log("아이디 확인확인:", userId);
        },
        error: function(error) {
            console.error('Error:', error);
        }
    });

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
                newUserPwd: newPwd,
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