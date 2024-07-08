$(document).ready(function() {
    let userId = null;

    // 현재 로그인된 사용자 ID 가져오기
    $.ajax({
        type: "GET",
        url: "/mypage/authenticatedUserId",
        success: function(response) {
            userId = response;
        },
        error: function(error) {
            console.error('Error:', error);
        }
    });

    // 비밀번호 확인
    function checkPassword() {
        const userPwd = $('#userPwd').val();

        $.ajax({
            type: "POST",
            url: "/mypage/pwdcheck",
            contentType: "application/json",
            data: JSON.stringify({
                userId: userId,
                userPwd: userPwd
            }),
            success: function(response) {
                if (response === "비밀번호가 일치합니다") {
                    $('#checkButton').html('<span class="material-symbols-outlined" id="check-icon">task_alt</span>');
                } else {
                    alert(response);
                }
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    }

    // 확인 버튼 클릭 이벤트 핸들러
    $('#checkButton').click(function() {
        checkPassword();
    });
});