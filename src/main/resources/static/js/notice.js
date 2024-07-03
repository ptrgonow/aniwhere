document.addEventListener('DOMContentLoaded', function () {
    const noticeData = [
        { 번호: 10, 카테고리: '이벤트', 제목: '신제품 할인 진행중', 작성일: '2024-05-20', 조회수: 0 },
        { 번호: 9, 카테고리: '이용방법', 제목: '이달의 추천 러너', 작성일: '2024-05-20', 조회수: 0 },
        { 번호: 8, 카테고리: '이벤트', 제목: '제품 후기 이벤트', 작성일: '2024-05-20', 조회수: 0 },
        { 번호: 7, 카테고리: '이벤트', 제목: '제품 후기 이벤트', 작성일: '2024-05-20', 조회수: 0 },
        { 번호: 6, 카테고리: '이벤트', 제목: '제품 후기 이벤트', 작성일: '2024-05-20', 조회수: 0 },
        { 번호: 5, 카테고리: '이벤트', 제목: '제품 후기 이벤트', 작성일: '2024-05-20', 조회수: 0 },
        // 더 많은 데이터 추가 가능
    ];

    const rowsPerPage = 5;
    let currentPage = 1;


    function displayTable(page) {
        const table = document.getElementById('notice_table');
        table.innerHTML = `
            <tr>
                <th>번호</th>
                <th>카테고리</th>
                <th>제목</th>
                <th>작성일</th>
                <th>조회수</th>
            </tr>
        `;

        const start = (page - 1) * rowsPerPage;
        const end = page * rowsPerPage;
        const paginatedItems = noticeData.slice(start, end);

        for (const item of paginatedItems) {
            const row = table.insertRow();
            row.innerHTML = `
                <td>${item.번호}</td>
                <td>${item.카테고리}</td>
                <td><a href="notice-detail.html">${item.제목}</a></td>
                <td>${item.작성일}</td>
                <td>${item.조회수}</td>
            `;
        }
    }

    function setupPagination() {
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
                displayTable(currentPage);
                setupPagination();
            });

            pagination.appendChild(pageItem);
        }
    }

    displayTable(currentPage);
    setupPagination();
});
