import {
    ClassicEditor,
    AccessibilityHelp,
    AutoImage,
    Autosave,
    Base64UploadAdapter,
    Bold,
    Essentials,
    ImageBlock,
    ImageInline,
    ImageInsert,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageToolbar,
    ImageUpload,
    Italic,
    Paragraph,
    SelectAll,
    Undo
} from 'ckeditor5';

const editorConfig = {
    toolbar: {
        items: ['undo', 'redo', '|', 'selectAll', '|', 'bold', 'italic', '|', 'insertImage', '|', 'accessibilityHelp'],
        shouldNotGroupWhenFull: false
    },
    plugins: [
        AccessibilityHelp,
        AutoImage,
        Autosave,
        Base64UploadAdapter,
        Bold,
        Essentials,
        ImageBlock,
        ImageInline,
        ImageInsert,
        ImageInsertViaUrl,
        ImageResize,
        ImageStyle,
        ImageToolbar,
        ImageUpload,
        Italic,
        Paragraph,
        SelectAll,
        Undo
    ],
    image: {
        toolbar: ['imageTextAlternative', '|', 'imageStyle:inline', 'imageStyle:wrapText', 'imageStyle:breakText', '|', 'resizeImage']
    },
    language: 'ko',

};


let editorInstance = null;

$(document).ready(function(){
    let limit = 10; // 페이지당 항목 수
    let offset = 0; // 시작점
    let isSearchMode = false;
    let searchQuery = '';

    fetchMembers('all', limit, offset);
    initializeModal();
    initializeCkEditor();
    initializeRemoveEmail();

    $('#empty-info').change(function() {
        const selectedOption = $(this).val();
        offset = 0; // 선택 변경 시 오프셋 초기화
        isSearchMode = false; // 전체 목록 모드로 전환
        fetchMembers(selectedOption, limit, offset);
    });

    $('#searchId').on('keypress', function(e) {
        if (e.which === 13) { // 엔터 키 코드
            const userId = $(this).val();
            offset = 0; // 엔터가 눌리면 페이지 오프셋 초기화
            isSearchMode = true;
            searchQuery = userId;
            searchUser(userId, limit, offset);
        }
    });

    // 전체 선택 체크박스의 상태에 따라 모든 개별 체크박스를 선택하거나 해제하는 이벤트
    $('#all-check').on('change', handleAllCheckChange);

    // 만약 개별 체크박스가 모두 선택되거나 해제되었을 때, 전체 선택 체크박스의 상태를 업데이트
    $('#member-tbody').on('change', '.mem-check', handleMemCheckChange);
    $('#sendMailButton').on('click', validateAndSendEmail);
});

function fetchMembers(type, limit, offset) {
    $.ajax({
        url: '/admin/dash/member/empty',
        method: 'GET',
        data: {
            type: type,
            limit: limit,
            offset: offset
        },
        success: function(response) {
            updateTable(response.members, offset);
            updatePagination(response.totalPages, response.currentPage, limit, offset, type, false, ''); // 검색 모드가 아님
        },
        error: function() {
            alert('Error');
        }
    });
}

function updateTable(members, offset) {
    const tbody = $('#member-tbody');
    tbody.empty();

    $.each(members, function(index, member) {
        let socialImage = '';
        if (member.social === true) {
            socialImage = '<img src="/img/social-person.png" alt="social" class="social-person">';
        }else {
            socialImage = '';
        }
        const row = `<tr>
            <td><input type="checkbox" class="mem-check" data-email="${member.email}"></td>
            <td>${offset + index + 1}</td>
            <td>${abbreviate(member.userId, 10)}</td>
            <td>${socialImage} ${member.userName}</td>
            <td>${member.email}</td>
            <td>${member.detailAddress}</td>
            <td>${member.phone || ''}</td>
            <td>${member.createdAt.substring(0, 10)}</td>
        </tr>`;
        tbody.append(row);
    });
    // 개별 체크박스가 변경될 때 all-check의 상태를 업데이트
    $('.mem-check').on('change', handleMemCheckChange);
}

function abbreviate(str, maxLength) {
    if (str.length > maxLength) {
        return str.substring(0, maxLength) + '...';
    }
    return str;
}

function updatePagination(totalPages, currentPage, limit, offset, typeOrQuery, isSearchMode, searchQuery) {
    const pagination = $('.pagination');
    pagination.empty();

    if (totalPages >= 1) {
        let paginationHtml = '';

        if (currentPage > 1) {
            paginationHtml += `<a href="#" class="page-link" data-page="${currentPage - 1}"><i class="fa fa-angle-left"></i></a>`;
        }

        for (let i = 1; i <= totalPages; i++) {
            paginationHtml += `<a href="#" class="page-link ${i === currentPage ? 'current' : ''}" data-page="${i}">${i}</a>`;
        }

        if (currentPage < totalPages) {
            paginationHtml += `<a href="#" class="page-link" data-page="${currentPage + 1}"><i class="fa fa-angle-right"></i></a>`;
        }

        pagination.html(paginationHtml);

        $('.page-link').click(function(e) {
            e.preventDefault();
            const page = $(this).data('page');
            let newOffset = (page - 1) * limit;
            if (isSearchMode) {
                searchUser(searchQuery, limit, newOffset); // 검색
            } else {
                fetchMembers(typeOrQuery, limit, newOffset); // 전체 목록
            }
        });
    }
}

function searchUser(userId, limit, offset) {
    $.ajax({
        url: '/admin/dash/member/search',
        method: 'GET',
        data: {
            userId: userId,
            limit: limit,
            offset: offset
        },
        success: function(response) {
            updateTable(response.users, offset);
            updatePagination(response.totalPages, response.currentPage, limit, offset, userId, true, userId); // 검색 모드로 설정
        },
        error: function() {
            alert('Error fetching users');
        }
    });
}

function handleAllCheckChange() {
    const isChecked = $(this).is(':checked');
    $('.mem-check').prop('checked', isChecked);
}

function handleMemCheckChange() {
    const allChecked = $('.mem-check').length === $('.mem-check:checked').length;
    $('#all-check').prop('checked', allChecked);
}

function initializeModal() {
    $('#mailSendModal').on('show.bs.modal', function (event) {
        var selectedEmails = [];
        $('.mem-check:checked').each(function(){
            selectedEmails.push($(this).data('email'));
        });

        // 받는 사람 필드에 이메일 주소를 개별적으로 추가
        const recipientContainer = $('#recipient-container');
        recipientContainer.empty();
        selectedEmails.forEach(email => {
            recipientContainer.append(`<span class="email-badge" data-email="${email}">${email} <span id="remove-email" class="remove-email" data-email="${email}">X</span></span>`);
        });
    });
}

function initializeRemoveEmail() {
    $(document).on('click', '.remove-email', function() {
        const email = $(this).data('email');
        $(this).parent().remove();
        $(`.mem-check[data-email="${email}"]`).prop('checked', false);
    });
}

// 이메일 전송
function sendEmails(disableDuration) {
    var to = [];
    var selectedCheckboxes = [];
    $('#recipient-container .email-badge').each(function(){
        var email = $(this).data('email');  // email 변수를 여기에서 정의
        to.push($(this).data('email'));
        var checkbox = $(`.mem-check[data-email="${email}"]`);
        selectedCheckboxes.push(checkbox);
    });

    var subject = $('#recipient-title').val();
    var body = editorInstance.getData();

    $.ajax({
        url: '/admin/dash/member/mailsend',
        method: 'POST',
        data: {
            to: to,
            subject: subject,
            body: body
        },
        success: function(response) {
            alert(response);
            $('#mailSendModal').modal('hide');
            // 체크박스를 주어진 시간 동안 비활성화
            disableCheckboxes(selectedCheckboxes, disableDuration);
        },
        error: function() {
            alert('Error sending emails');
        }
    });
}

// 이메일 발송 회원 30초 동안 비활성화
function disableCheckboxes(checkboxes, duration) {
    checkboxes.forEach(function(checkbox) {
        checkbox.prop('disabled', true);
    });

    setTimeout(function() {
        checkboxes.forEach(function(checkbox) {
            checkbox.prop('disabled', false);
        });
    }, duration);
}

function validateAndSendEmail() {
    const recipientCount = $('#recipient-container .email-badge').length;
    const title = $('#recipient-title').val().trim();
    const body = editorInstance.getData();

    if (recipientCount === 0) {
        alert('받는 사람을 선택하세요.');
        return;
    }
    if (title === '') {
        alert('제목을 입력하세요.');
        return;
    }
    if (body === '') {
        alert('내용을 입력하세요.');
        return;
    }
    sendEmails(30000);  // 30초 동안 비활성화
}

function initializeCkEditor() {

    ClassicEditor.create(
        document.querySelector('#editor'), editorConfig)
        .then(editor => {
            editorInstance = editor;
        })
        .catch(error => {
                console.error(error);
            }
        )

}
