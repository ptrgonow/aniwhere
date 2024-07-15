document.addEventListener('DOMContentLoaded', function () {
    const paginationContainers = {
        all: document.getElementById('pagination-container-all'),
        dog: document.getElementById('pagination-container-dog'),
        cat: document.getElementById('pagination-container-cat'),
        others: document.getElementById('pagination-container-other')
    };

    const categories = document.querySelectorAll('.product-filters ul li');

    function getParameterByName(name, url = window.location.href) {
        name = name.replace(/[\[\]]/g, '\\$&');
        const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
        const results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }

    const currentCategory = getParameterByName('category') || 'all';

    categories.forEach(category => {
        category.addEventListener('click', function () {
            const selectedCategory = this.getAttribute('data-category');
            categories.forEach(cat => cat.classList.remove('active'));
            this.classList.add('active');

            document.querySelectorAll('.category-container').forEach(container => {
                if (container.classList.contains(selectedCategory)) {
                    container.classList.add('active');
                } else {
                    container.classList.remove('active');
                }
            });

            // 페이지를 1로 설정하여 첫 번째 페이지로 이동
            window.location.href = `/shop/main?page=1&size=9&category=${selectedCategory}`;
        });

        if (category.getAttribute('data-category') === currentCategory) {
            category.classList.add('active');
        }
    });

    document.querySelectorAll('.category-container').forEach(container => {
        if (container.classList.contains(currentCategory)) {
            container.classList.add('active');
        }
    });

    function createPaginationLinks(category) {
        const paginationContainer = paginationContainers[category];
        var currentPage = parseInt(document.getElementById('currentPage').value);
        var totalPages = parseInt(document.getElementById('totalPages').value);

        const rangeSize = 9;
        const currentRangeStart = Math.floor((currentPage - 1) / rangeSize) * rangeSize + 1;
        const currentRangeEnd = Math.min(currentRangeStart + rangeSize - 1, totalPages);


        let links = '';
        if (currentRangeStart > 1) {
            links += `<a href="/shop/main?page=${currentRangeStart - rangeSize}&size=9&category=${category}" class="page-link">&laquo;</a>`;
        }
        for (let i = currentRangeStart; i <= currentRangeEnd; i++) {
            links += `<a href="/shop/main?page=${i}&size=9&category=${category}" class="page-link ${i === currentPage ? 'active' : ''}">${i}</a>`;
        }
        if (currentRangeEnd < totalPages) {
            links += `<a href="/shop/main?page=${currentRangeStart + rangeSize}&size=9&category=${category}" class="page-link">&raquo;</a>`;
        }
        paginationContainer.innerHTML = links;
    }

    createPaginationLinks(currentCategory);

    window.scrollToTop = function () {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    window.scrollToBottom = function () {
        window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
    }


});


