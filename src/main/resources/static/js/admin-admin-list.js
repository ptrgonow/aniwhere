function showAdminDetails(element) {
    var adminId = element.innerText;
    fetchAdminDetails(adminId);
}

function fetchAdminDetails(adminId) {
    fetch('/admin/details/' + adminId)
        .then(response => response.json())
        .then(data => {
            var adminDetailsDiv = document.getElementById('admin-list-detail');
            adminDetailsDiv.innerHTML = `
                <div class="page-wrapper">
                    <div class="container d-flex justify-content-center">
                        <div class="join col-xl-8 col-8 d-flex justify-content-center w-100 h-100">
                            <div id="adminDetails" class="w-100">
                                <div class="form-input">
                                    <label class="reg-form">아이디</label>
                                    <p class="in-text">${data.userId}</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">비밀번호</label>
                                    <p class="in-text">********</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">이름</label>
                                    <p class="in-text">${data.userName}</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">이메일</label>
                                    <p class="in-text">${data.email}</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">주소</label>
                                    <p class="in-text">${data.address} ${data.detailAddress}, ${data.zipCode}</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">휴대폰</label>
                                    <p class="in-text">${data.phone}</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">역할</label>
                                    <p class="in-text">${data.role}</p>
                                </div>
                                <div class="form-input">
                                    <label class="reg-form">시작 날짜</label>
                                    <p class="in-text">${data.createdAt}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        });
}
