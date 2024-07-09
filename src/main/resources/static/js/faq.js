document.addEventListener('DOMContentLoaded', function () {
    const menuItems = document.querySelectorAll('.menu');
    const allAccordionSections = document.querySelectorAll('.accordion > div'); // 모든 FAQ 섹션 선택
    const allAccordionItems = document.querySelectorAll('.accordion-item');

    // 동적으로 id와 data-bs-target 설정
    allAccordionItems.forEach((item, index) => {
        const button = item.querySelector('.accordion-button');
        const collapseDiv = item.querySelector('.accordion-collapse');

        // 고유한 id 생성
        const collapseId = `collapse${index + 1}`;

        // button과 collapseDiv에 id 및 data-bs-target 설정
        button.setAttribute('data-bs-target', `#${collapseId}`);
        button.setAttribute('aria-controls', collapseId);
        collapseDiv.setAttribute('id', collapseId);
    });

    menuItems.forEach(item => {
        item.addEventListener('click', function () {
            // 모든 메뉴 항목에서 active 클래스를 제거
            menuItems.forEach(i => i.classList.remove('active'));

            // 클릭된 항목에 active 클래스 추가
            this.classList.add('active');

            // 모든 FAQ 섹션을 숨김
            allAccordionSections.forEach(section => section.classList.add('d-none'));

            // 클릭된 항목의 data-target 값을 가져와서 해당 FAQ 섹션을 표시
            const target = this.getAttribute('data-target');
            document.querySelector(`.${target}`).classList.remove('d-none');
        });
    });
});
