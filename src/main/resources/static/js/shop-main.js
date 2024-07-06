function scrollToTop(){
    window.scrollTo({top: 0, behavior: 'smooth' });
}

function scrollToBottom(){
    window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth' });
}

window.addEventListener('scroll', () => {
    const scrollButtons = document.querySelector('.scroll-buttons-custom');
    if(window.scrollY > 100) {
        scrollButtons.classList.add('visible');
    }else{
        scrollButtons.classList.remove('visible');
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // Add event listeners to toggle buttons
    document.querySelector('.toggle-view-custom.grid-view-custom').addEventListener('click', function() {
        document.querySelector(".product-lists-custom").classList.add("grid-view-custom");
        document.querySelector(".product-lists-custom").classList.remove("list-view-custom");
    });

    document.querySelector('.toggle-view-custom.list-view-custom').addEventListener('click', function() {
        document.querySelector(".product-lists-custom").classList.add("list-view-custom");
        document.querySelector(".product-lists-custom").classList.remove("grid-view-custom");
    });

    // Default to grid view
    document.querySelector(".product-lists-custom").classList.add("grid-view-custom");

    // Product filters
    const filterButtons = document.querySelectorAll('.product-filters-custom li');
    const products = document.querySelectorAll('.product-lists-custom .col-lg-4, .product-lists-custom .col-md-6');

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
