document.addEventListener('DOMContentLoaded', () => {
    // 페이지 로드 시 grid 클래스 추가
    document.querySelector('.products').classList.add('grid');

    const pContElements = document.querySelectorAll('.list .p-cont span');
    pContElements.forEach(pCont => {
        // 텍스트 넓이 확인
        const textWidth = pCont.scrollWidth;
        const containerWidth = pCont.offsetWidth;

        // 텍스트가 컨테이너보다 넓을 경우에만 애니메이션 적용
        if (textWidth > containerWidth) {
            pCont.style.animation = 'marquee 10s linear infinite'; // 애니메이션 이름 설정
            pCont.style.paddingLeft = '100%'; // 초기 위치 설정
        }

        // 마우스 오버 시 애니메이션 멈춤
        pCont.addEventListener('mouseenter', () => {
            pCont.style.animationPlayState = 'paused';
        });

        // 마우스 떼면 애니메이션 재개
        pCont.addEventListener('mouseleave', () => {
            pCont.style.animationPlayState = 'running';
        });
    });
});

function changeStyle(style) {
    const productsContainer = document.querySelector('.products');
    productsContainer.className = `products ${style}`;
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

