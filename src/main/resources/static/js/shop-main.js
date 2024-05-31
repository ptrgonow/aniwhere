/* 메인 화면 제품 진열 방식 */
function changeStyle(style){
    const productsContainer = document.querySelector('.products');
    productsContainer.className=`products ${style}`;
}

/* 맨 위 아래 이동 버튼 */
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

/* 사이드 바 탭 버튼 카테고리/브랜드 */
function showTab(tab){
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(t => t.classList.remove('active'));

    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(b => b.classList.remove('active'));

    document.getElementById(tab).classList.add('active');
    document.querySelector(`.tab-button[onclick="showTab('${tab}')"]`).classList.add('active');
}

/* 사이드 바 탭 버튼 카테고리/브랜드 => 알파벳 순서 */
function filterBrands(initial){
    const brands = document.querySelectorAll('.brand-item');
    brands.forEach(brand => {
        if(initial === 'all' || brand.dataset.initial === initial){
            brand.style.display = '';
        }else{
            brand.style.display = 'none';
        }
    });
}
