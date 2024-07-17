$(document).ready(function(){
    fetchAdminList();
    fetchAuthenticatedUserInfo();
});

let currentOpenAdminId = null;

function fetchAuthenticatedUserInfo() {
    $.ajax({
        url: '/auth/userinfo', // 인증된 사용자 정보를 가져오는 엔드포인트
        method: 'GET',
        success: function(response) {
            $('#authUserName').text(response.name);
            $('#authUserId').text(response.userId);
        },
        error: function() {
            alert('사용자 정보를 불러오는데 실패했습니다.=_=');
        }
    });
}

function fetchAdminList() {
    $.ajax({
        url: '/admin/regi/list',
        method: 'GET',
        success: function(response) {
            var adminList = $('#adminList');
            adminList.empty();
            response.forEach(function(admin) {
                adminList.append(`
                    <tr onclick="toggleAdminDetails('${admin.userId}')">
                        <td>${admin.userId}</td>
                        <td>${admin.userName}</td>
                        <td>${admin.createdAt}</td>
                    </tr>
                `);
            });
        },
        error: function() {
            alert('관리자 목록을 불러오는데 실패했습니다.');
        }
    });
}

function toggleAdminDetails(adminId) {
    if (currentOpenAdminId === adminId) {
        // 이미 열려 있는 경우 닫기
        $('#admin-list-detail').empty();
        currentOpenAdminId = null;
    } else {
        // 새로 열기
        showAdminDetails(adminId);
    }
}

function showAdminDetails(adminId) {
    $.ajax({
        url: '/admin/regi/details/' + adminId,
        method: 'GET',
        success: function(response) {
            var adminDetailsDiv = $('#admin-list-detail');
            adminDetailsDiv.html(`
                <div class="page-wrapper">
                    <div class="container d-flex justify-content-center">
                        <div class="join col-xl-8 col-8 d-flex justify-content-center w-100 h-100">
                            <div id="adminDetails" class="w-100">
                                <div class="form-input">
                                    <label class="reg-form">아이디</label>
                                    <p class="in-text">${response.userId}</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">이름</label>
                                    <input type="text" id="userName" value="${response.userName}" />
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">이메일</label>
                                    <input type="email" id="email" value="${response.email}" />
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">주소</label>
                                    <input type="text" id="address" value="${response.address}" />
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">상세 주소</label>
                                    <input type="text" id="detailAddress" value="${response.detailAddress}" />
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">우편번호</label>
                                    <input type="text" id="zipCode" value="${response.zipCode}" />
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">휴대폰</label>
                                    <input type="text" id="phone" value="${response.phone}" />
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">역할</label>
                                    <p class="in-text">${response.role}</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">시작 날짜</label>
                                    <p class="in-text">${response.createdAt}</p>
                                </div>
                                <div class="form-input">
                                    <button onclick="updateAdminDetails('${response.userId}')">수정</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `);
            currentOpenAdminId = adminId;
        },
        error: function() {
            alert('관리자 정보를 불러오는데 실패했습니다.');
        }
    });
}

function updateAdminDetails(userId) {
    var adminDetails = {
        userId: userId,
        userName: $('#userName').val(),
        email: $('#email').val(),
        address: $('#address').val(),
        detailAddress: $('#detailAddress').val(),
        zipCode: $('#zipCode').val(),
        phone: $('#phone').val()
    };

    $.ajax({
        url: '/admin/regi/details/update',
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(adminDetails),
        success: function(response) {
            alert(response);
            fetchAdminList(); // 수정 후 관리자 목록을 다시 불러옴
        },
        error: function() {
            alert('관리자 정보 수정에 실패했습니다.');
        }
    });
}
