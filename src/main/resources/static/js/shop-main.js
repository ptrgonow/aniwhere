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
