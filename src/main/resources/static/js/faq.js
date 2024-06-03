document.addEventListener('DOMContentLoaded', function() {
    const menuItems = document.querySelectorAll('.menu');
    const sections = document.querySelectorAll('.q_area > div[id]');

    menuItems.forEach(item => {
        item.addEventListener('click', function() {
            const targetId = item.getAttribute('data-target');

            // Hide all sections
            sections.forEach(section => {
                section.style.display = 'none';
            });

            // Show the target section
            const targetSection = document.getElementById(targetId);
            if (targetSection) {
                targetSection.style.display = 'block';
            }

            // Reset the color of all menu items
            menuItems.forEach(menu => {
                menu.style.backgroundColor = '';
            });

            // Set the color of the clicked menu item
            item.style.backgroundColor = '#febe98';
        });
    });

    // Optionally, display the first section by default
    if (sections.length > 0) {
        sections[0].style.display = 'block';
        menuItems[0].style.backgroundColor = '#febe98';
    }
});

const accordionBtns = document.querySelectorAll('.q_accordion button');

accordionBtns.forEach(btn => {
    btn.addEventListener('click', (event) => {
        const targetSection = btn.nextElementSibling;
        const content = targetSection.querySelector('.faq_a');

        if (targetSection.classList.contains('active')) {
            targetSection.classList.remove('active');
            content.style.maxHeight = 0;
        } else {
            accordionBtns.forEach(otherBtn => {
                otherBtn.nextElementSibling.classList.remove('active');
                otherBtn.nextElementSibling.querySelector('.faq_a').style.maxHeight = 0;
            });

            targetSection.classList.add('active');
            content.style.maxHeight = content.scrollHeight + 'px';
        }
    });
});


document.addEventListener('DOMContentLoaded', function() {
    const toggleBtns = document.querySelectorAll('.toggle-btn');

    toggleBtns.forEach(btnWrapper => {
        const toggleBtn = btnWrapper.querySelector('button');
        const content = btnWrapper.nextElementSibling;

        content.style.maxHeight = '0';

        toggleBtn.addEventListener('click', () => {
            if (content.style.maxHeight === '0px') {
                content.style.maxHeight = content.scrollHeight + 'px';
                toggleBtn.querySelector('i').classList.replace('fa-chevron-down', 'fa-chevron-up');
            } else {
                content.style.maxHeight = '0';
                toggleBtn.querySelector('i').classList.replace('fa-chevron-up', 'fa-chevron-down');
            }
        });
    });
});
