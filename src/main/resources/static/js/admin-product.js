$(document).ready(function () {
    initializeFilters();
    initializeSearchButton();

    // 초기 페이지네이션 설정
    const initialCurrentPage = parseInt($('#initialCurrentPage').val());
    const initialTotalPages = parseInt($('#initialTotalPages').val());
    const initialCategory = $('#initialCategory').val();
    initializePagination(initialCurrentPage, initialTotalPages, initialCategory);


});
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
                window.location.href = `/admin/products?category=${category}&page=${page}&size=9`;
            }
        });
    }

    createPagination(currentPage, totalPages, 5);
}
function initializeFilters() {
    $('.product-filters li').on('click', function () {
        var selectedCategory = $(this).data('category');
        window.location.href = `/admin/products?category=${selectedCategory}&page=1&size=9`;
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
        const productName = product.name.length > 12 ? product.name.substring(0, 12) + "..." : product.name;
        const productHtml = `
        <tr>
            <td><input type="checkbox" class="product-checkbox" value="${product.productId}"></td>
            <td class="product-image">
                    <img src="${product.image}" alt="상품 이미지">
            </td>
            <td>${productName}</td>
            <td>${product.price.replace(',', '')}원</td>
            <td>
                <input type="text" class="inventory" value="${product.quantity}">
                <button class="btn btn-success update-quantity-btn" data-product-id="${product.productId}">변경</button>
            </td>
            <td>${product.category}</td>
            <td>${new Date(product.createdAt).toLocaleDateString('ko-KR')}</td>
            <td>
                <div class="actions">
                    <a href="/admin/product/edit?id=${product.productId}" class="btn btn-primary">수정</a>
                    <button class="btn btn-danger delete-product-btn" data-product-id="${product.productId}">삭제</button>
                </div>
            </td>
        </tr>
    `;
        productContainer.append(productHtml);
    });
}
