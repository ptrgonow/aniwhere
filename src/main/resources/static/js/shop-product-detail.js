document.addEventListener('DOMContentLoaded', function() {
    const priceElement = document.getElementById('product-price');
    const quantityElement = document.getElementById('quantity');
    const totalPriceElement = document.getElementById('total-price');

    // 가격 정보 가져오기
    const productPrice = parseFloat(priceElement.textContent);

    // 수량이 변경될 때 총 가격 업데이트
    quantityElement.addEventListener('input', function() {
        const quantity = parseInt(quantityElement.value);
        const totalPrice = productPrice * quantity;
        totalPriceElement.textContent = totalPrice.toFixed(2);
    });
});
