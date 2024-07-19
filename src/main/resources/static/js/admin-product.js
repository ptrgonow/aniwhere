$(document).ready(function () {
    initializeFilters();
    initializeSearchButton();

    // 초기 페이지네이션 설정
    const initialCurrentPage = parseInt($('#initialCurrentPage').val());
    const initialTotalPages = parseInt($('#initialTotalPages').val());
    const initialCategory = $('#initialCategory').val();
    initializePagination(initialCurrentPage, initialTotalPages, initialCategory);

    $(document).on('click', '.delete-product-btn', function() {
        const productId = $(this).data('productId');

        // 삭제 확인 다이얼로그
        if (confirm('정말로 상품을 삭제하시겠습니까?')) {
            deleteProduct(productId);
        }
    });

    const image = document.getElementById('image');
    const imagePreview = document.getElementById('imagePreview');

    image.addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                imagePreview.src = e.target.result;
                imagePreview.style.display = 'block';
            }
            reader.readAsDataURL(file);
        } else {
            imagePreview.src = '#';
            imagePreview.style.display = 'none';
        }
    });

    const editImageInput = document.getElementById('new-image');
    const editImagePreview = document.getElementById('new-imagePreview');

    editImageInput.addEventListener('change', handleImagePreview(editImageInput, editImagePreview));

    function handleImagePreview(input, preview) {
        return function() {
            const file = input.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    preview.src = e.target.result;
                    preview.style.display = 'block';
                }
                reader.readAsDataURL(file);
            } else {
                preview.src = '#';
                preview.style.display = 'none';
            }
        }
    }

    $(document).on('click', '.btn.btn-primary.edit', function() {
        const productId = $(this).data('productId');
        fetchProductDetails(productId);
    });

    $('#update-product').on('click', function() {
        const productId = $('#product-id').val();

        const formData = new FormData();

        formData.append('productId', productId);
        formData.append('name', $('#new-name').val());
        formData.append('price', $('#new-price').val().replace(/,/g, ''));
        formData.append('category', $('#new-category').val());
        formData.append('quantity', $('#new-quantity').val());


        const imageInput = $('#new-image')[0].files[0];
        const detailImageInput = $('#new-detail-image')[0].files[0];
        console.log(imageInput, detailImageInput)
        const reader = new FileReader();
        reader.onloadend = function () {
            const base64Image = reader.result.split(',')[1];
            formData.append('image', base64Image);


            if (detailImageInput) {
                const detailReader = new FileReader();
                detailReader.onloadend = function () {
                    const base64DetailImage = detailReader.result.split(',')[1];
                    formData.append('detailUrl', base64DetailImage);
                    editProduct(formData); // 상품 정보 전송
                };
                detailReader.readAsDataURL(detailImageInput);
            } else {
                editProduct(formData);
            }
        };
        reader.readAsDataURL(imageInput);

    });

    // 상품 등록 버튼 클릭 이벤트 핸들러
    $('.btn.btn-light-primary.insert').on('click', function () {
        const formData = new FormData();

        const imageInput = $('#image')[0].files[0];
        const detailImageInput = $('#detail-image')[0].files[0];
        const name = $('#name').val();
        const price = $('#price').val();
        const quantity = $('#quantity').val();
        const category = $('#category').val();

        formData.append('name', name);
        formData.append('price', price);
        formData.append('category', category);
        formData.append('quantity', quantity);
        console.log(name, price,category, quantity)
        // 이미지 파일 처리
        const reader = new FileReader();
        reader.onloadend = function () {
            const base64Image = reader.result.split(',')[1];
            formData.append('image', base64Image);

            // 상세 이미지 처리 (detailImageInput 존재 여부 확인)
            if (detailImageInput) {
                const detailReader = new FileReader();
                detailReader.onloadend = function () {
                    const base64DetailImage = detailReader.result.split(',')[1];
                    formData.append('detailUrl', base64DetailImage);
                    submitProduct(formData); // 상품 정보 전송
                };
                detailReader.readAsDataURL(detailImageInput);
            } else {
                submitProduct(formData); // 상세 이미지 없이 상품 정보 전송
            }
        };
        reader.readAsDataURL(imageInput);
    });
});
ㅈㅁ
function submitProduct(formData) {
    const url = '/admin/dash/products/add';

    callAjax(url, 'POST', JSON.stringify(Object.fromEntries(formData.entries())), function(data) {
        console.log("Review saved successfully: ", data);

        alert("상품이 등록되었습니다.")
        window.location.reload()
    }, function(error) {
        console.log("Error saving review: ", error);
    }, {
        processData: false,
        contentType: 'application/json'
    });
}

function editProduct(formData) {
    const url = '/admin/dash/products/edit';

    callAjax(url, 'PUT', JSON.stringify(Object.fromEntries(formData.entries())), function(data) {
        console.log("Review saved successfully: ", data);

        alert("상품이 수정되었습니다.")

        window.location.reload();
    }, function(error) {
        console.log("Error saving review: ", error);
    }, {
        processData: false,
        contentType: 'application/json'
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
        url: `/admin/product?keyword=${keyword}&page=${page}&size=9`,
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
        const productImage = product.image.startsWith('https://')
            ? `<img src="${product.image}" alt="상품 이미지">`
            : `<img src="data:image/png;base64,${product.image}" alt="상품 이미지">`;

        const productHtml = `
        <tr>
            <td class="product-image">
                 ${productImage}
            </td>
            <td>${productName}</td>
            <td>${product.price.replace(',', '')}원</td>
            <td>
                 ${product.quantity}
            </td>
            <td>${product.category}</td>
            <td>${new Date(product.createdAt).toLocaleDateString('ko-KR')}</td>
            <td>
                <div class="actions">
                     <button class="btn btn-primary edit" data-product-id="${product.productId}"
                                                        data-bs-toggle="modal" data-bs-target="#new-staticBackdrop">수정</button>
                    <button class="btn btn-danger delete-product-btn" data-product-id="${product.productId}">삭제</button>
                </div>
            </td>
        </tr>
    `;
        productContainer.append(productHtml);
    });
}

function callAjax(url, method, data, successCallback, errorCallback, options = {}) {
    $.ajax({
        url: url,
        type: method,
        data: data,
        processData: options.processData !== undefined ? options.processData : true,
        contentType: options.contentType !== undefined ? options.contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
        success: successCallback,
        error: errorCallback
    });
}

function fetchProductDetails(productId) {
    $.ajax({
        url: `/admin/dash/products/${productId}`,
        method: 'GET',
        dataType: 'json',
        success: function(product) {
            // 모달에 상품 정보 채우기
            $('#new-name').val(product.name);
            $('#new-price').val(product.price);
            $('#new-quantity').val(product.quantity);
            $('#product-id').val(product.productId);
            console.log(product.productId)
            const categorySelect = $('#new-category');
            const category = product.category;
            let selectedCategory = '';

            if (category.includes('강아지')) {
                selectedCategory = '강아지';
            } else if (category.includes('고양이')) {
                selectedCategory = '고양이';
            } else if (category.includes('공용')) {
                selectedCategory = '공용';
            }

            categorySelect.val(selectedCategory);

            if (product.image) {
                $('#new-imagePreview').attr('src', product.image.startsWith('https://') ? product.image : `data:image/png;base64,${product.image}`);
                $('#new-imagePreview').show();
            }
        },
        error: function(xhr, status, error) {
            console.error('상품 정보 조회 실패:', error);
            alert('상품 정보를 가져오는 중 오류가 발생했습니다.');
        }
    });
}

function deleteProduct(productId) {
    $.ajax({
        url: '/admin/dash/products/delete/' + productId, // 상품 삭제 엔드포인트
        method: 'DELETE',
        success: function(response) {
            alert('상품이 삭제되었습니다.');
            location.reload(); // 페이지 새로고침
        },
        error: function(xhr, status, error) {
            console.error('상품 삭제 실패:', error);
            alert('상품 삭제 중 오류가 발생했습니다.');
        }
    });
}
