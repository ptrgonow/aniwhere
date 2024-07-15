document.addEventListener('DOMContentLoaded', function () {
    const deleteButton = document.getElementById('delete-button');

    if (deleteButton) {
        deleteButton.addEventListener('click', function (event) {
            event.preventDefault(); // 링크의 기본 동작을 막습니다.

            const noticeId = this.getAttribute('data-id');

            if (confirm("정말로 이 공지사항을 삭제하시겠습니까?")) {
                fetch(`/board/delete-notice?id=${noticeId}`, { // 경로를 /board/delete-notice로 변경합니다.
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            alert('공지사항이 삭제되었습니다.');
                            location.href = '/board/notice';  // 목록 페이지로 리다이렉트
                        } else {
                            alert('삭제에 실패했습니다.');
                        }
                    })
                    .catch(error => {
                        console.error('삭제 요청 중 오류 발생:', error);
                        alert('삭제 요청 중 오류가 발생했습니다.');
                    });
            }
        });
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const noticeId = new URLSearchParams(window.location.search).get('id');

    if (noticeId) {
        // 공지사항 상세 정보 가져오기
        fetch(`/api/notice/detail?id=${noticeId}`)
            .then(response => response.json())
            .then(data => {
                displayNoticeDetail(data);
            })
            .catch(error => console.error('Error fetching notice details:', error));
    }

    function displayNoticeDetail(notice) {
        const titleElement = document.querySelector('.detail_title h3');
        const dateElement = document.querySelector('.info_body.date');
        const hitElement = document.querySelector('.info_body.hit');
        const contentElement = document.querySelector('.detail_content p');

        titleElement.textContent = notice.title;
        dateElement.textContent = formatDate(notice.createdAt);
        hitElement.textContent = notice.hit;
        contentElement.innerHTML = notice.content;
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }
});


