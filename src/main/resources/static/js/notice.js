document.addEventListener('DOMContentLoaded', function () {
    const rowsPerPage = 8;
    let currentPage = 1;

/*

안녕하세요 아래 매서드는 ㅇㅇㅇㅇㅇ 기능입니다.


*/
    function displayTable(noticeData, page) {
        const tableBody = document.querySelector('#notice_table tbody');
        tableBody.innerHTML = ''; // 기존 테이블 내용을 지우기

        if (noticeData.length === 0) {
            const row = tableBody.insertRow();
            const cell = row.insertCell(0);
            cell.colSpan = 4;
            cell.textContent = '이런 내용은 없는디유;;;';
            cell.style.textAlign = 'center';
            return;
        }

        const start = (page - 1) * rowsPerPage;
        const end = page * rowsPerPage;
        const paginatedItems = noticeData.slice(start, end);


        // 최신 게시글이 번호 1이 되도록 역순 번호 매기기
        for (const [index, item] of paginatedItems.entries()) {
            const row = tableBody.insertRow();
            const rowNumber = (noticeData.length - (start + index)); // 역순 번호 계산
            row.innerHTML = `
        <td>${rowNumber}</td>
        <td><a href="/board/notice-detail?id=${item.noticeId}">${item.title}</a></td>
        <td class="date">${formatDate(item.createdAt)}</td>
        <td>${item.hit}</td>
    `;

            // 행 클릭 이벤트 추가
            row.addEventListener('click', () => {
                window.location.href = `/board/notice-detail?id=${item.noticeId}`;
            });

            // 링크에 클릭 이벤트 버블링 막기
            const link = row.querySelector('a');
            link.addEventListener('click', (event) => {
                event.stopPropagation();
            });
        }

    }

    function setupPagination(noticeData) {
        const pagination = document.getElementById('pagination');
        pagination.innerHTML = '';

        const pageCount = Math.ceil(noticeData.length / rowsPerPage);
        for (let i = 1; i <= pageCount; i++) {
            const pageItem = document.createElement('a');
            pageItem.href = '#';
            pageItem.innerText = i;
            pageItem.className = (i === currentPage) ? 'active' : '';

            pageItem.addEventListener('click', function (e) {
                e.preventDefault();
                currentPage = i;
                displayTable(noticeData, currentPage);
                setupPagination(noticeData);

            });

            pagination.appendChild(pageItem);
        }
    }


    function formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    function fetchNotices(query = '') {
        const url = query ? `/search?query=${query}` : '/list';
        fetch(url)
            .then(response => response.json())
            .then(data => {
                displayTable(data, currentPage);
                setupPagination(data);
            })
            .catch(error => console.error('Error fetching notices:', error));
    }

    // 초기 데이터 로드
    fetchNotices();

    // 검색 요청 처리
    document.querySelector('.search_form button').addEventListener('click', function (e) {
        e.preventDefault();
        const query = document.querySelector('.search_form input[name="search_text"]').value;
        fetchNotices(query);
    });

    // 엔터 키 검색 처리
    document.querySelector('.search_form input[name="search_text"]').addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            const query = document.querySelector('.search_form input[name="search_text"]').value;
            fetchNotices(query);
        }
    });
});
