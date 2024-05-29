document.addEventListener('DOMContentLoaded', function () {
    const modals = document.querySelectorAll('.modal');
    const closeButtons = document.querySelectorAll('.close');

    function openModal(modalId) {
        document.getElementById(modalId).style.display = 'block';
    }

    function closeModal(modalId) {
        document.getElementById(modalId).style.display = 'none';
    }

    window.openModal = openModal; // 글로벌로 함수 노출
    window.closeModal = closeModal; // 글로벌로 함수 노출

    closeButtons.forEach(button => {
        button.addEventListener('click', function () {
            closeModal(this.parentElement.parentElement.id);
        });
    });

    window.addEventListener('click', function (event) {
        modals.forEach(modal => {
            if (event.target === modal) {
                closeModal(modal.id);
            }
        });
    });
});
