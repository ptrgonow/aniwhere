document.addEventListener('DOMContentLoaded', function () {
    CKEDITOR.replace('editor', {
        toolbar: [
            { name: 'editing', items: ['Undo', 'Redo'] },
            { name: 'basicstyles', items: ['Bold', 'Italic', 'Underline'] },
            { name: 'links', items: ['Link'] },
            { name: 'insert', items: ['Image', 'Table'] },
            { name: 'paragraph', items: ['NumberedList', 'BulletedList'] },
            { name: 'styles', items: ['Format'] }
        ],
        language: 'ko',
        height: 500
    });

    document.getElementById('mail-form').addEventListener('submit', function (e) {
        e.preventDefault(); // 기본 폼 제출 방지

        const titleInput = document.querySelector('input[name="title"]');
        const title = titleInput ? titleInput.value.trim() : '';
        let editorValue = CKEDITOR.instances.editor.getData().trim();

        // 불필요한 HTML 태그와 &nbsp; 제거
        editorValue = editorValue.replace(/&nbsp;/g, ' ')
            .replace(/<p><\/p>/g, '')
            .replace(/<[^>]+>/g, '');

        let errorMessages = [];

        if (title === '') {
            errorMessages.push('제목을 입력하세요.');
        }

        if (editorValue === '') {
            errorMessages.push('내용을 입력하세요.');
        }

        if (errorMessages.length > 0) {
            alert(errorMessages.join('\n'));
        } else {
            // 폼 데이터를 JSON으로 변환
            const formData = {
                title: title,
                content: editorValue
            };

            // AJAX 요청 보내기
            fetch('/submit-mail', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            })
                .then(response => response.json())  // 서버 응답을 JSON으로 처리
                .then(result => {
                    if (result.status === 'success') {  // 서버 응답이 'success'인 경우
                        alert('메일 전송 완료');
                        // 성공 시 대시보드로 리디렉션
                        window.location.href = '/admin/dashboard';
                    } else {
                        alert('메일 전송 실패: ' + (result.message || '알 수 없는 오류'));
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('메일 전송 실패');
                });
        }
    });
});
