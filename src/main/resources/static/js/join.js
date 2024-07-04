

$(document).ready(function() {

    $('#join-btn').on('click', function() {
        joinProgress();
    });

});

function joinProgress() {

    let join = {
        userId: $('#userId').val(),
        userPwd: $('#userPwd').val(),
        userName: $('#userName').val(),
        email: $('#email').val(),
        address: $('#address').val(),
        detailAddress: $('#detailAddress').val(),
        zipCode: $('#zipCode').val(),
        phone: $('#phone').val()
    }

    console.log(join);

    $.ajax({
        type: 'POST',
        url: '/joinProc',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(join)
    }).done(function() {
        alert('회원가입이 완료되었습니다.');
        location.href = '/login';
    }).fail(function(error) {
        alert(JSON.stringify(error));
    });

}
