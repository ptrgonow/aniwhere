document.addEventListener('DOMContentLoaded', function () {
    const menuItems = document.querySelectorAll('.menu');

    menuItems.forEach(item => {
        item.addEventListener('click', function () {
            // 모든 메뉴 항목에서 active 클래스를 제거
            menuItems.forEach(i => i.classList.remove('active'));

            // 클릭된 항목에 active 클래스 추가
            this.classList.add('active');
        });
    });
});