$(document).ready(function(){
    fetchAdminList(3, 0); // 초기 로드 시 3명씩 가져오기
    fetchAuthenticatedUserInfo();

    $('#searchId').on('keypress', function(e) {
        if (e.which === 13) { // Enter key
            searchAdmin();
        }
    });

    $('#searchButton').on('click', searchAdmin);

    $('#roleSelect').on('change', function() {
        fetchAdminList(3, 0, $(this).val());
    });

    $('#closePanel').on('click', closeSlidePanel);

    $('#prevPage').on('click', function() {
        changePage('prev');
    });

    $('#nextPage').on('click', function() {
        changePage('next');
    });

    $('#firstPage').on('click', function() {
        fetchAdminList(3, 0, $('#roleSelect').val());
    });

    $('#lastPage').on('click', function() {
        fetchAdminList(3, (totalPages - 1) * 3, $('#roleSelect').val());
    });
});

let currentOpenAdminId = null;
let currentPage = 1;
let totalPages = 0;

function fetchAuthenticatedUserInfo() {
    $.ajax({
        url: '/admin/regi/userinfo', // 인증된 사용자 정보를 가져오는 엔드포인트
        method: 'GET',
        success: function(response) {
            $('#authUserName').text(response.userName);
            $('#authUserId').text(response.userId);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error("Error details: ", textStatus, errorThrown, jqXHR.responseText);
            alert('사용자 정보를 불러오는데 실패했습니다.');
        }
    });
}

function fetchAdminList(limit = 3, offset = 0, role = 'ROLE_ADMIN') {
    $.ajax({
        url: '/admin/regi/list',
        method: 'GET',
        data: {
            limit: limit,
            offset: offset,
            role: role
        },
        success: function(response) {
            console.log(response); // 응답 데이터 로그 출력
            var adminList = $('#adminList');
            adminList.empty();
            if (response.admins && Array.isArray(response.admins)) {
                response.admins.forEach(function(admin) {
                    adminList.append(`
                        <tr onclick="toggleAdminDetails('${admin.userId}')">
                            <td>${admin.userId}</td>
                            <td>${admin.userName}</td>
                            <td>${admin.createdAt}</td>
                        </tr>
                    `);
                });
                if (response.admins.length === 0) {
                    adminList.append('<tr><td colspan="3">관리자가 없습니다.</td></tr>');
                }
            } else {
                adminList.append('<tr><td colspan="3">관리자가 없습니다.</td></tr>');
            }
            totalPages = response.totalPages;
            currentPage = response.currentPage;
            updatePagination();
        },
        error: function(xhr, status, error) {
            console.error('Error fetching admin list:', xhr, status, error);
            alert('관리자 목록을 불러오는데 실패했습니다.');
        }
    });
}

function updatePagination() {
    var pagination = $('#pageNumbers');
    pagination.empty();

    var startPage = Math.max(currentPage - 1, 1);
    var endPage = Math.min(startPage + 2, totalPages);

    for (var i = startPage; i <= endPage; i++) {
        var activeClass = (i === currentPage) ? 'active' : '';
        pagination.append(`<span class="page-link ${activeClass}" onclick="fetchAdminList(3, ${(i - 1) * 3}, $('#roleSelect').val())">${i}</span>`);
    }

    $('#prevPage').toggleClass('disabled', currentPage === 1);
    $('#nextPage').toggleClass('disabled', currentPage === totalPages);
    $('#firstPage').toggleClass('disabled', currentPage === 1);
    $('#lastPage').toggleClass('disabled', currentPage === totalPages);
}

function changePage(direction) {
    if (direction === 'prev' && currentPage > 1) {
        fetchAdminList(3, (currentPage - 2) * 3, $('#roleSelect').val());
    } else if (direction === 'next' && currentPage < totalPages) {
        fetchAdminList(3, currentPage * 3, $('#roleSelect').val());
    }
}

function searchAdmin() {
    var query = $('#searchId').val();
    var role = $('#roleSelect').val();
    if (!query) {
        alert('아이디 또는 이름을 입력하세요.');
        return;
    }

    $.ajax({
        url: '/admin/regi/search',
        method: 'GET',
        data: { query: query, role: role },
        success: function(response) {
            var adminList = $('#adminList');
            adminList.empty();
            if (response.admins && Array.isArray(response.admins)) {
                if (response.admins.length === 0) {
                    alert('검색 결과가 없습니다 JS.');
                    fetchAdminList(3, 0, role); // 검색 결과 없을 시 초기 목록 로드
                } else {
                    $('#admin-list-detail').empty();
                    response.admins.forEach(function(admin) {
                        adminList.append(`
                            <tr onclick="toggleAdminDetails('${admin.userId}')">
                                <td>${admin.userId}</td>
                                <td>${admin.userName}</td>
                                <td>${admin.createdAt}</td>
                            </tr>
                        `);
                    });
                    totalPages = response.totalPages;
                    currentPage = response.currentPage;
                    updatePagination();
                }
            } else {
                alert('검색 결과가 없습니다.JS2');
                fetchAdminList(3, 0, role); // 검색 결과 없을 시 초기 목록 로드
            }
        },
        error: function(xhr, status, error) {
            console.error('Error searching admin:', xhr, status, error);
            alert('관리자 검색에 실패했습니다.');
            fetchAdminList(3, 0, role); // 검색 실패 시 초기 목록 로드
        }
    });
}

function toggleAdminDetails(adminId) {
    if (currentOpenAdminId === adminId) {
        closeSlidePanel();
    } else {
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
                    <input type="checkbox" id="roleSwitch" ${response.role === 'ROLE_ADMIN' ? 'checked' : ''} data-toggle="switch" data-on-text="관리자" data-off-text="일반 회원" />
                </div>
                <div class="form-input">
                    <label class="reg-form">시작 날짜</label>
                    <p class="in-text">${response.createdAt}</p>
                </div>
                <div class="form-input">
                    <button onclick="updateAdminDetails('${response.userId}')">수정</button>
                </div>
            `);
            $("[data-toggle='switch']").bootstrapSwitch(); // 부트스트랩 스위치 초기화
            openSlidePanel();
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
        phone: $('#phone').val(),
        role: $('#roleSwitch').bootstrapSwitch('state') ? 'ROLE_ADMIN' : 'ROLE_USER'
    };

    $.ajax({
        url: '/admin/regi/details/update',
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(adminDetails),
        success: function(response) {
            alert(response);
            fetchAdminList(3, 0, $('#roleSelect').val()); // 수정 후 관리자 목록을 다시 불러옴
        },
        error: function() {
            alert('관리자 정보 수정에 실패했습니다.');
        }
    });
}

function openSlidePanel() {
    $('#slidePanel').addClass('open');
}

function closeSlidePanel() {
    $('#slidePanel').removeClass('open');
    $('#admin-list-detail').empty();
    currentOpenAdminId = null;
}
