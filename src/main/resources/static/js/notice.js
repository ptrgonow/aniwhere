document.addEventListener('DOMContentLoaded', function () {
    const rowsPerPage = 8;
    let currentPage = 1;

    function displayTable(noticeData, page) {
        const tableBody = document.querySelector('#notice_table tbody');
        tableBody.innerHTML = ''; // 기존 테이블 내용을 지우기

        const start = (page - 1) * rowsPerPage;
        const end = page * rowsPerPage;
        const paginatedItems = noticeData.slice(start, end);

        for (const item of paginatedItems) {
            const row = tableBody.insertRow();
            row.innerHTML = `
                <td>${item.noticeId}</td>
                <td><a href="/board/notice-detail?id=${item.noticeId}">${item.title}</a></td>
                <td class="date">${formatDate(item.createdAt)}</td>
                <td>${item.hit}</td>
            `;
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

    // 데이터베이스에서 공지사항 데이터를 가져오기
    fetch('/list')
        .then(response => response.json())
        .then(data => {
            displayTable(data, currentPage);
            setupPagination(data);
        })
        .catch(error => console.error('Error fetching notices:', error));
});
