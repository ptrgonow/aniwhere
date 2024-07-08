document.addEventListener("DOMContentLoaded", function() {
    const pageSize = 9;

    const productData = [];
    document.querySelectorAll('#product-container .single-product-item').forEach(item => {
        productData.push({
            image: item.querySelector('.product-image img').src,
            name: item.querySelector('.product-details h3').innerText,
            price: parseInt(item.querySelector('.product-details .product-price p').innerText.replace(/,/g, ''), 10)
        });
    });

    $('#pagination-container').pagination({
        dataSource: productData,
        pageSize: pageSize,
        callback: function(data, pagination) {
            const container = $('#product-container');
            container.empty();

            data.forEach(product => {
                const productDiv = `
                <div class="col-lg-4 col-md-6 text-center">
                    <div class="single-product-item">
                       <div class="product-image">
                            <a><img src="${product.image}" alt="Product Image"></a>
                        </div>
                        <div class="product-details">
                            <h3>${product.name}</h3>
                        <div class="product-price">
                                <p>${product.price}원</p> 
                            </div>
                        </div>
                        <a class="cart-btn"><i class="fas fa-shopping-cart"></i> Add to Cart</a>
                    </div>
                </div>`;
                container.append(productDiv);
            });
        }
    });
});



function scrollToTop(){
    window.scrollTo({top: 0, behavior: 'smooth' });
}

function scrollToBottom(){
    window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth' });
}

window.addEventListener('scroll', () => {
    const scrollButtons = document.querySelector('.scroll-buttons');
    if(window.scrollY > 100) {
        scrollButtons.classList.add('visible');
    }else{
        scrollButtons.classList.remove('visible');
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // Add event listeners to toggle buttons
    document.querySelector('.toggle-view.grid-view').addEventListener('click', function() {
        document.querySelector(".product-lists").classList.add("grid-view");
        document.querySelector(".product-lists").classList.remove("list-view");
    });

    document.querySelector('.toggle-view.list-view').addEventListener('click', function() {
        document.querySelector(".product-lists").classList.add("list-view");
        document.querySelector(".product-lists").classList.remove("grid-view");
    });

    // Default to grid view
    document.querySelector(".product-lists").classList.add("grid-view");

    // Product filters
    const filterButtons = document.querySelectorAll('.product-filters li');
    const products = document.querySelectorAll('.product-lists .col-lg-4');

    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
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

