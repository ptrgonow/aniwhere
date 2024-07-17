$(document).ready(function () {
    initializeCartButton();
    initializeFilters();
    initializeSearchButton();

    // 초기 페이지네이션 설정
    const initialCurrentPage = parseInt($('#initialCurrentPage').val());
    const initialTotalPages = parseInt($('#initialTotalPages').val());
    const initialCategory = $('#initialCategory').val();
    initializePagination(initialCurrentPage, initialTotalPages, initialCategory);
});

function initializeCartButton() {
    $(document).on('click', '.cart-btn', function (event) {
        event.preventDefault();
        const productId = $(this).closest('.single-product-item').find('.product-image a').attr('href').split('=')[1];
        const quantity = 1;

        $.ajax({
            url: "/cart/add/" + productId + "?quantity=" + quantity,
            type: "POST",
            success: function (response) {
                alert(response);
            },
            error: function (xhr, status, error) {
                alert("이미 장바구니에 존재하는 상품입니다");
            }
        });
    });
}

function initializePagination(currentPage = 1, totalPages = 1, category = '', keyword = '') {
    const paginationContainer = $('#pagination-container');

    function createPagination(current, total, visiblePages) {
        let start = Math.floor((current - 1) / visiblePages) * visiblePages + 1;
        let end = Math.min(start + visiblePages - 1, total);

        paginationContainer.empty();

        if (start > 1) {
            paginationContainer.append(`<a class="page-link" href="#" data-page="${start - 1}" data-category="${category}" data-keyword="${keyword}"><i class="fa fa-angle-left"></i></a>`);
        } else {
            paginationContainer.append(`<span class="page-link disabled"><i class="fa fa-angle-left"></i></span>`);
        }

        for (let i = start; i <= end; i++) {
            let pageLink = $(`<a class="page-link" href="#" data-page="${i}" data-category="${category}" data-keyword="${keyword}">${i}</a>`);
            if (i === current) {
                pageLink.addClass('current');
            }
            paginationContainer.append(pageLink);
        }

        if (end < total) {
            paginationContainer.append(`<a class="page-link" href="#" data-page="${end + 1}" data-category="${category}" data-keyword="${keyword}"><i class="fa fa-angle-right"></i></a>`);
        } else {
            paginationContainer.append(`<span class="page-link disabled"><i class="fa fa-angle-right"></i></span>`);
        }

        // 페이지 링크 클릭 이벤트 처리
        $('.page-link').on('click', function (event) {
            event.preventDefault();
            const page = $(this).data('page');
            const category = $(this).data('category');
            const keyword = $(this).data('keyword');

            if (keyword) {
                searchProducts(page, keyword);
            } else {
                window.location.href = `/shop/main?category=${category}&page=${page}&size=9`;
            }
        });
    }

    createPagination(currentPage, totalPages, 5);
}

function initializeFilters() {
    $('.product-filters li').on('click', function () {
        var selectedCategory = $(this).data('category');
        window.location.href = `/shop/main?category=${selectedCategory}&page=1&size=9`;
    });
}

function initializeSearchButton() {
    $('#search-btn').on('click', function () {
        searchProducts();
    });
}

function searchProducts(page = 1, keyword = '') {
    if (!keyword) {
        keyword = $('#searchKeyword').val();
    }

    if (keyword === '') {
        alert('검색어를 입력하세요');
        return;
    }

    $.ajax({
        url: `/shop/product?keyword=${keyword}&page=${page}&size=9`,
        type: 'GET',
        success: function (response) {
            displaySearchResults(response.products);
            initializePagination(response.currentPage, response.totalPages, '', keyword);
        },
        error: function (xhr, status, error) {
            alert('검색 결과가 없습니다');
        }
    });
}

function displaySearchResults(products) {
    const productContainer = $('#product-container');
    productContainer.empty();

    products.forEach(product => {
        const productHtml = `
            <div class="col-lg-4 col-md-6 text-center">
                <div class="single-product-item">
                    <div class="product-image">
                        <a href="/shop/detail?id=${product.productId}">
                            <img src="${product.image}" alt="제품 이미지">
                        </a>
                    </div>
                    <div class="product-details">
                        <h3>${product.name}</h3>
                        <div class="product-price">
                            <p>${product.price}</p>원
                        </div>
                    </div>
                    <a class="cart-btn"><i class="fas fa-shopping-cart"></i> Add to Cart</a>
                </div>
            </div>
        `;
        productContainer.append(productHtml);
    });
}

window.scrollToTop = function () {
    window.scrollTo({ top: 0, behavior: 'smooth' });
};

window.scrollToBottom = function () {
    window.scrollTo({ top: $(document).height(), behavior: 'smooth' });
};
