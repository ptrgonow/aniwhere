$(document).ready(function() {

    // // 페이지 로드 시 카카오 로그인 및 사용자 정보 가져오기
    // Kakao.Auth.login({
    //     success: function(authObj) {
    //         fetchUserInfo();
    //     },
    //     fail: function(err) {
    //         console.log(err);
    //     }
    // });
    //
    // function fetchUserInfo() {
    //     Kakao.API.request({
    //         url: '/v2/user/me',
    //         data: {
    //             property_keys: ['kakao_account.email', ' kakao_account.profile.nickname'],
    //         },
    //         success: function(response) {
    //             console.log(response);
    //             $('#userName').val(response.kakao_account.profile.nickname);
    //             $('#email').val(response.kakao_account.email);
    //         },
    //         fail: function(error) {
    //             console.log(error);
    //         }
    //     });
    // }

    // 소셜 로그인 여부 확인 및 비밀번호 필드 비활성화
    const isSocial = $('#hiddenSocial').val();
    if (isSocial === 'true') {
        $('#userPwd').val('소셜로그인 회원입니다.').prop('disabled', true).addClass('disabled-input');
        $('#userNewPwd').val('소셜로그인 회원입니다.').prop('disabled', true).addClass('disabled-input');
        $('#confirmNewPwd').val('소셜로그인 회원입니다.').prop('disabled', true).addClass('disabled-input');
        $('#checkButton').prop('hidden', true).addClass('hidden');
        $('.pwd-info').text('* 소셜로그인 회원으로 비밀번호를 변경할 수 없습니다.');
    }

    const userId = $('#hiddenUserId').val();
    let isPasswordChecked = false;

    // 비밀번호 확인
    function checkPassword() {
        const userPwd = $('#userPwd').val();
        if (!userId) {
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
            success: function (response) {
                alert(response);
                if (response === "비밀번호가 일치합니다") {
                    $('#checkButton').html('<span class="material-symbols-outlined" id="check-icon">task_alt</span>');
                    isPasswordChecked = true; // 비밀번호 확인 성공 시 플래그 설정
                } else {
                    isPasswordChecked = false; // 비밀번호가 일치하지 않으면 플래그 초기화
                    alert('비밀번호가 일치하지 않습니다');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', xhr.responseText);
                alert('비밀번호 확인에 실패했습니다.');
                isPasswordChecked = false;
            }
        });
    }

    // 확인 버튼 클릭 이벤트 핸들러
    $('#checkButton').click(function () {
        checkPassword();
    });

    // 새로운 비밀번호 입력 일치 확인
    $('#confirmNewPwd').on('input', function () {
        const newPassword = $('#userNewPwd').val();
        const confirmPassword = $('#confirmNewPwd').val();
        const message = $('#pwd-message');

        if (newPassword === confirmPassword) {
            message.css('display', 'block').text('비밀번호가 일치합니다').removeClass('error').addClass('success');
        } else {
            message.css('display', 'block').text('비밀번호가 일치하지 않습니다').removeClass('success').addClass('error');
        }
    });

    // 서버로부터 전달된 데이터를 JavaScript 변수에 할당
    const loggedInUserName = $('#hiddenUserName').val();
    const loggedInPhone = $('#hiddenUserPhone').val();

    function checkDuplicateFields(field) {
        const userName = $("#userName").val();
        const phone = $("#phone").val();
        const nameMessage = $("#name-message");
        const phoneMessage = $("#phone-message");

        if (field === "userName") {
            if (userName.length < 2) {
                nameMessage.text("이름은 2글자 이상 입력해야 합니다.").removeClass("success").addClass("error");
                return;
            }
            if (userName === loggedInUserName) {
                nameMessage.text("현재 사용하고 있는 이름입니다.").removeClass("error").addClass("success");
                return;
            }
            $.ajax({
                type: "GET",
                url: "/mypage/checkdupl",
                data: { userName: userName, phone: "" },
                success: function (response) {
                    if (response.nameExists) {
                        nameMessage.text("사용 불가능한 이름입니다.").removeClass("success").addClass("error");
                    } else {
                        nameMessage.text("사용 가능한 이름입니다.").removeClass("error").addClass("success");
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error: ", status, error);
                }
            });
        }

        if (field === "phone") {
            if (!/^\d{11}$/.test(phone)) {
                phoneMessage.text("휴대폰 형식에 맞게 입력하세요").removeClass("success").addClass("error");
                return;
            }
            if (phone === loggedInPhone) {
                phoneMessage.text("현재 사용하고 있는 휴대폰입니다.").removeClass("error").addClass("success");
                return;
            }
            $.ajax({
                type: "GET",
                url: "/mypage/checkdupl",
                data: { userName: "", phone: phone },
                success: function (response) {
                    if (response.phoneExists) {
                        phoneMessage.text("사용 불가능한 휴대폰입니다.").removeClass("success").addClass("error");
                    } else {
                        phoneMessage.text("사용 가능한 휴대폰입니다.").removeClass("error").addClass("success");
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error: ", status, error);
                }
            });
        }
    }

    $("#userName").on("input", function () {
        checkDuplicateFields("userName");
    });

    $("#phone").on("input", function () {
        checkDuplicateFields("phone");
    });

    $("#modi-btn").on("click", function () {
        if (!isPasswordChecked && isSocial !== 'true') {
            alert('현재 비밀번호를 확인해 주세요');
            return;
        }
        const userId = $("#hiddenUserId").val();
        const newPwd = $("#userNewPwd").val();
        const confirmPwd = $("#confirmNewPwd").val();
        const userName = $("#userName").val();
        const address = $("#address").val();
        const detailAddress = $("#detailAddress").val();
        const zipCode = $("#zipCode").val();
        const phone = $("#phone").val();

        if (newPwd && newPwd !== confirmPwd) {
            alert('새로 입력한 비밀번호가 일치하지 않습니다');
            $('#confirmNewPwd').focus();
            return;
        }

        if (userName === '') {
            alert('이름을 입력하세요');
            $("#userName").focus();
            return;
        } else if (userName.length < 2) {
            alert('이름을 2글자 이상 입력하세요');
            $("#userName").focus();
            return;
        }

        if (address === '') {
            alert('주소를 입력하세요');
            $("#address").focus();
            return;
        } else if (zipCode === '') {
            alert('우편번호를 입력하세요');
            $("#zipCode").focus();
            return;
        }

        const phoneRegex = /^\d{11}$/;
        if (phone === '') {
            alert('휴대폰 번호를 입력하세요');
            $("#phone").focus();
            return;
        } else if (!phoneRegex.test(phone)) {
            alert('휴대폰 형식에 맞게 입력하세요');
            $("#phone").focus();
            return;
        }

        // 정보 수정
        $.ajax({
            type: "PUT",
            url: "/mypage/update",
            contentType: "application/json",
            data: JSON.stringify({
                userId: userId,
                newUserPwd: newPwd ? newPwd : null,
                userName: userName,
                address: address,
                detailAddress: detailAddress,
                zipCode: zipCode,
                phone: phone
            }),
            success: function (response) {
                alert("정보 수정이 완료되었습니다.");
                location.reload();
            },
            error: function (xhr, error) {
                console.error('Error:', error);
                if (xhr.responseText === '이미 사용중인 이름입니다. 다시 입력하세요') {
                    alert('이미 사용중인 이름입니다. 다시 입력하세요');
                } else if (xhr.responseText === '이미 사용중인 휴대폰입니다. 다시 입력하세요') {
                    alert('이미 사용중인 휴대폰입니다. 다시 입력하세요');
                } else {
                    alert(xhr.responseText);
                }
            }
        });
    });

    // 회원탈퇴 버튼 클릭 이벤트 핸들러
    $('#del-btn').click(function() {
        if (!isPasswordChecked && isSocial !== 'true') {
            alert('현재 비밀번호를 확인해 주세요');
            return;
        }

        if (confirm("정말로 회원탈퇴를 하시겠습니까?")) {
            $.ajax({
                type: "DELETE",
                url: "/mypage/userdel/" + userId,
                success: function(response) {
                    alert("회원탈퇴가 완료되었습니다.");
                    // 로그아웃 요청
                    $.ajax({
                        type: "POST",
                        url: "/logout",
                        success: function() {
                            window.location.href = "/login?logout=true&message=logoutSuccess"; // 로그아웃 후 로그인 페이지로 이동
                        },
                        error: function(xhr, status, error) {
                            console.error('Error:', xhr.responseText);
                            alert('로그아웃에 실패했습니다. 다시 시도해주세요.');
                        }
                    });
                },
                error: function(xhr, status, error) {
                    console.error('Error:', xhr.responseText);
                    alert('회원탈퇴에 실패했습니다. 다시 시도해주세요.');
                }
            });
        }
    });
});

// 주소 찾기
function searchAdd() {
    new daum.Postcode({
        oncomplete: function (data) {
            let addr = ''; // 주소
            let extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
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