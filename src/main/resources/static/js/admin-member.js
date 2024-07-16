$(document).ready(function(){

    let limit = 10; // 페이지당 항목 수
    let offset = 0; // 시작점
    let currentPage = 1;

    // 초기 로드 시 전체 회원 조회
    fetchMembers('all', limit, offset);

    $('#empty-info').change(function() {
        const selectedOption = $(this).val();
        offset = 0; // 선택 변경 시 오프셋 초기화
        fetchMembers(selectedOption);
    });

});

function fetchMembers(type, limit, offset) {
    $.ajax({
        url: '/member-empty',
        method: 'GET',
        data: {
            type: type,
            limit: limit,
            offset: offset
        },
        success: function(response) {
            updateTable(response, offset);
            updatePagination(response.totalPages, response.currentPage);
        },
        error: function() {
            alert('Error' + 'error');
        }
    });
}

function updateTable(members, offset) {
    const tbody = $('table tbody');
    tbody.empty();

    $.each(members, function(index, member) {
        const row = '<tr>' +
            '<td><input type="checkbox" name="mem-check"></td>' +
            '<td>' + (offset + index + 1) + '</td>' +
            '<td>' + abbreviate(member.userId, 10) + '</td>' +
            '<td>' + member.userName + '</td>' +
            '<td>' + member.email + '</td>' +
            '<td>' + (member.address || '') + ' ' + (member.detailAddress || '') + '</td>' +
            '<td>' + (member.phone || '') + '</td>' +
            '<td>' + (member.createdAt).substring(0,10) + '</td>' +
            '</tr>';
        tbody.append(row);
    });
}

function abbreviate(str, maxLength) {
    if (str.length > maxLength) {
        return str.substring(0, maxLength) + '...';
    }
    return str;
}

function updatePagination(totalPages, currentPage) {
    const pagination = $('.pagination');
    pagination.empty();

    if (currentPage > 1) {
        pagination.append('<a href="#" class="page-link" data-page="' + (currentPage - 1) + '">&laquo;</a>');
    }

    for (let i = 1; i <= totalPages; i++) {
        pagination.append('<a href="#" class="page-link" data-page="' + i + '">' + i + '</a>');
    }

    if (currentPage < totalPages) {
        pagination.append('<a href="#" class="page-link" data-page="' + (currentPage + 1) + '">&raquo;</a>');
    }

    $('.page-link').click(function(e) {
        e.preventDefault();
        const page = $(this).data('page');
        let offset = (page - 1) * limit;
        fetchMembers($('#empty-info').val(), limit, offset);
    });
}