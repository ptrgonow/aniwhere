document.addEventListener('DOMContentLoaded', function() {
    const gridViewBtn = document.querySelector('.toggle-view.grid-view');
    const listViewBtn = document.querySelector('.toggle-view.list-view');
    const newsContainer = document.querySelector('.latest-news');

    // Ensure grid view is set by default
    newsContainer.classList.add('grid-view');
    newsContainer.classList.remove('list-view');

    gridViewBtn.addEventListener('click', function() {
        newsContainer.classList.add('grid-view');
        newsContainer.classList.remove('list-view');
    });

    listViewBtn.addEventListener('click', function() {
        newsContainer.classList.add('list-view');
        newsContainer.classList.remove('grid-view');
    });
});
