document.addEventListener("DOMContentLoaded", function() {
    const pageSize = 9;
    let currentPage = 1;

    const allProducts = /*[[${products}]]*/ []; // 전체 상품 데이터 가져오기
    const productContainer = document.getElementById('product-container');
    const paginationContainer = document.getElementById('pagination-container');

    // 필터링 및 페이지네이션 함수
    function renderProducts(category, page) {
        currentPage = page;

        // 필터링된 상품 데이터 가져오기
        let filteredProducts = allProducts;
        if (category !== '*') {
            if (category === 'others') {
                filteredProducts = allProducts.filter(product =>
                    !product.name.includes('강아지') && !product.name.includes('고양이')
                );
            } else {
                filteredProducts = allProducts.filter(product => product.name.includes(category));
            }
        }

        // 페이지네이션 계산
        const totalPages = Math.ceil(filteredProducts.length / pageSize);
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = Math.min(startIndex + pageSize, filteredProducts.length);

        // 상품 목록 업데이트
        productContainer.innerHTML = '';
        for (let i = startIndex; i < endIndex; i++) {
            const product = filteredProducts[i];
            const productElement = `
                <div class="col-lg-4 col-md-6 text-center ${product.category}">
                    <div class="single-product-item">
                        <div class="product-image">
                            <a><img src="${product.image}" alt="외부 이미지"></a>
                        </div>
                        <div class="product-details">
                            <h3>${product.name}</h3>
                            <div class="product-price">
                                <p>${product.price}원</p>
                            </div>
                        </div>
                        <a class="cart-btn"><i class="fas fa-shopping-cart"></i> Add to Cart</a>
                    </div>
                </div>
            `;
            productContainer.insertAdjacentHTML('beforeend', productElement);
        }

    }



    // 현재 필터 가져오기 함수
    function getCurrentFilter() {
        return document.querySelector('.product-filters li.active').dataset.filter;
    }

    // 필터 버튼 클릭 이벤트 처리
    filterButtons.forEach(button => {
        button.addEventListener('click', function () {
            filterButtons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
            renderProducts(this.dataset.filter, 1); // 첫 페이지부터 다시 렌더링
        });
    });

    // 초기 전체 상품 렌더링
    renderProducts('*', 1); // 초기에는 전체 상품 표시

    // 보기 방식 토글 버튼 클릭 이벤트 처리 (applyViewMode 함수 필요)
    const toggleButtons = document.querySelectorAll('.toggle-view');
    toggleButtons.forEach(button => {
        button.addEventListener('click', () => {
            toggleButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');

            const view = button.classList.contains('grid-view') ? 'grid-view' : 'list-view';
            productContainer.classList.remove('grid-view', 'list-view');
            productContainer.classList.add(view);
        });
    });

    function scrollToTop() {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }

    function scrollToBottom() {
        window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});
    }

    window.addEventListener('scroll', () => {
        const scrollButtons = document.querySelector('.scroll-buttons');
        if (window.scrollY > 100) {
            scrollButtons.classList.add('visible');
        } else {
            scrollButtons.classList.remove('visible');
        }
    });

    document.addEventListener("DOMContentLoaded", function () {
        // Add event listeners to toggle buttons
        document.querySelector('.toggle-view.grid-view').addEventListener('click', function () {
            document.querySelector(".product-lists").classList.add("grid-view");
            document.querySelector(".product-lists").classList.remove("list-view");
        });

        document.querySelector('.toggle-view.list-view').addEventListener('click', function () {
            document.querySelector(".product-lists").classList.add("list-view");
            document.querySelector(".product-lists").classList.remove("grid-view");
        });

        // Default to grid view
        document.querySelector(".product-lists").classList.add("grid-view");

        // Product filters
        const filterButtons = document.querySelectorAll('.product-filters li');
        const products = document.querySelectorAll('.product-lists .col-lg-4');

        filterButtons.forEach(button => {
            button.addEventListener('click', function () {
                // Remove active class from all buttons and add to the clicked button
                filterButtons.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');

                // Get the filter class
                const filter = this.getAttribute('data-filter');

                // Show/Hide products
                products.forEach(product => {
                    if (filter === '*' || product.classList.contains(filter.substring(1))) {
                        product.style.display = 'block';
                    } else {
                        product.style.display = 'none';
                    }
                });
            });
        });
    });
});

